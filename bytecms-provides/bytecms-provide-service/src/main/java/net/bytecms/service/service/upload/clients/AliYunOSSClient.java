package net.bytecms.service.service.upload.clients;

import com.aliyun.oss.*;
import com.aliyun.oss.model.AppendObjectRequest;
import com.aliyun.oss.model.AppendObjectResult;
import com.aliyun.oss.model.PutObjectRequest;
import com.google.common.collect.Lists;
import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.config.ThinkCmsConfig;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.FileUtil;
import net.bytecms.service.dto.resource.SysResourceDto;
import net.bytecms.service.dto.upload.Chunk;
import net.bytecms.service.service.upload.UploadClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
@Component
public class AliYunOSSClient extends UploadClient {
    private ClientBuilderConfiguration conf;
    private OSS ossClient ;

    @Autowired
    ThinkCmsConfig thinkCmsConfig;

    @Autowired
    BaseRedisService baseRedisService;

    @PostConstruct
    public void init(){
        accessKey = thinkCmsConfig.getOss().getAccessKey();
        secretKey = thinkCmsConfig.getOss().getSecretKey();
        endpoint=thinkCmsConfig.getOss().getEndpoint();
        bucket = thinkCmsConfig.getOss().getBucket();
        prefix=thinkCmsConfig.getOss().getPrefix();
        conf = new ClientBuilderConfiguration();
    }

    @Override
    public ApiResult uploadFile(MultipartFile file) {
        PutObjectRequest putObjectRequest = null;
        try {
            String key= UUID.randomUUID().toString()+ Constants.DOT +FileUtil.getSuffix(file.getOriginalFilename());
            putObjectRequest = new PutObjectRequest(bucket,key, file.getInputStream());
            try {
                ossClient.putObject(putObjectRequest);
            }catch ( OSSException|ClientException e){
                return  ApiResult.result(20004);
            }
            Map<String,String> uploadRes = new HashMap<>();
            uploadRes.put("filePath",key);
            uploadRes.put("fileFullPath",prefix+key);
            uploadRes.put("group",bucket);
            uploadRes.put("path",key);
            uploadRes.put("fileName",file.getOriginalFilename());
            return ApiResult.result(uploadRes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ossClient.shutdown();
        }
        return  ApiResult.result(20004);
    }

    @Override
    public ApiResult keepUploadFile(Chunk chunk) {
        Map<String,Object> param=new HashMap<>();
        MultipartFile file=chunk.getFile();
        String key=chunk.getIdentifier()+ Constants.DOT +FileUtil.getSuffix(file.getOriginalFilename());
        try {
            AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucket, key, file.getInputStream());
            if(chunk.getChunkNumber().intValue()==1){
                appendObjectRequest.setPosition(0L);
            }else{
                appendObjectRequest.setPosition( getPosition(chunk));
            }
            try {
                AppendObjectResult appendObjectResult = ossClient.appendObject(appendObjectRequest);
                param.put("fileName",file.getOriginalFilename());
                param.put("fileFullPath", prefix+key);
                markUpload(chunk,appendObjectRequest,appendObjectResult,param);
            }catch ( OSSException|ClientException e){
                return  ApiResult.result(20004);
            }
            return ApiResult.result(param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResult.result();
    }
    private Long getPosition(Chunk chunk){
        String fileNext=Constants.fastDfsKeepUpload+chunk.getIdentifier()+"_next";
        Object nextNum=baseRedisService.get(fileNext);
        if(Checker.BeNotNull(nextNum)){
            return ((Integer)nextNum).longValue();
        }else{
            return null;
        }
    }

    private boolean markUpload(Chunk chunk,AppendObjectRequest appendObjectRequest,AppendObjectResult appendObjectResult,Map<String,Object> param){
        boolean isLastChunk=chunk.getChunkNumber().intValue()==chunk.getTotalChunks().intValue();
        String fileKey=Constants.fastDfsKeepUpload+chunk.getIdentifier();
        String fileNext=Constants.fastDfsKeepUpload+chunk.getIdentifier()+"_next";
        param.put("fileMd5",chunk.getIdentifier());
        if(isLastChunk){
            param.put("finshed",true);
            baseRedisService.removeByKeys(Arrays.asList(fileKey,fileNext));
        }else{
            param.put("finshed",false);
            List<Integer> uploadChunk = ( List<Integer>)baseRedisService.get(fileKey);
            if(Checker.BeEmpty(uploadChunk)){
                uploadChunk=new ArrayList<>();
            }
            uploadChunk.add(chunk.getChunkNumber());
            baseRedisService.set(fileKey,uploadChunk);
            baseRedisService.set(fileNext,appendObjectResult.getNextPosition());
        }
        return true;
    }

    @Override
    public ApiResult readyUpload(MultipartFile file) {
        if(Checker.BeNotNull(file)){
            reloadOss();
            return  ApiResult.result();
        }
        return  ApiResult.result(20026);
    }

    @Override
    public ApiResult uploadSuccess(ApiResult apiResult, MultipartFile file) {
        return saveFileToDb(apiResult,file);
    }

    @Override
    public void uploadError(ApiResult result) {

    }

    @Override
    public ApiResult deleteFile(Map<String, String> params) {
        reloadOss();
        String uid=params.get("fileUid");
        SysResourceDto resourceDto=getFileByUid(uid);
        if(Checker.BeNotNull(resourceDto)){
            String filePath=resourceDto.getFilePath();
            String groupName=resourceDto.getGroupName();
            if(Checker.BeNotBlank(filePath)){
                try {
                    ossClient.deleteObject(groupName, filePath);
                }finally {
                    ossClient.shutdown();
                }
                deleteFileByUid(uid);
                return ApiResult.result(filePath);
            }else{
                return ApiResult.result(20018);
            }
        }
        return ApiResult.result();
    }

    @Override
    public Map<String,Object>  checkFileIsExistByMd5(String md5) {
        SysResourceDto resource=checkerHasFileByMd5(md5);
        Map<String,Object> params=new HashMap<>(16);
        if(Checker.BeNotNull(resource)){
            params.put("filePath",resource.getFilePath());
            params.put("fileFullPath",resource.getFileFullPath());
            params.put("fileName",resource.getFileName());
            params.put("fileUid",resource.getFileUid());
            params.put("group",resource.getGroupName());
            params.put("id",resource.getId());
            params.put("path",resource.getFilePath());
            params.put("skip",true);
        }else{
            params.put("skip",false);
            String fileKey=Constants.fastDfsKeepUpload+md5;
            List<Integer> uploadChunk = ( List<Integer>)baseRedisService.get(fileKey);
            params.put("uploadChunk",Checker.BeNotEmpty(uploadChunk)?uploadChunk: Lists.newArrayList());
        }
        return params;
    }

    private void reloadOss(){
        this.ossClient= new OSSClientBuilder().build(endpoint, accessKey, secretKey, conf);
    }
}
