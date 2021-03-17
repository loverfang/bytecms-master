package net.bytecms.service.service.upload.clients;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.config.ByteCmsConfig;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.service.dto.resource.SysResourceDto;
import net.bytecms.service.dto.upload.Chunk;
import net.bytecms.service.service.upload.UploadClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// TODO 文件上传 client 多选一 根据注解 DefaultUploadClient指定唯一的 client 只需要集继承 UploadClient 并将client 纳入spring
//管理 @Component
@Component
public class QiNiuYunClient extends UploadClient {

    private Configuration cfg;

    private UploadManager uploadManager;

    private Auth auth;

    private BucketManager bucketManager;

    @PostConstruct
    public void init(){
        accessKey = byteCmsConfig.getOss().getAccessKey();
        secretKey = byteCmsConfig.getOss().getSecretKey();
        prefix= byteCmsConfig.getOss().getPrefix();
        bucket = byteCmsConfig.getOss().getBucket();
        cfg = new Configuration(Region.region0());
        uploadManager = new UploadManager(cfg);
        auth = Auth.create(accessKey, secretKey);
        bucketManager = new BucketManager(auth, cfg);
    }

    @Autowired
    BaseRedisService baseRedisService;

    @Autowired
    ByteCmsConfig byteCmsConfig;

    @Override
    public ApiResult uploadFile(MultipartFile file) {
        String upToken = auth.uploadToken(bucket);
        DefaultPutRet putRet=null;
        try {
             Response response = uploadManager.put(file.getInputStream(), null, upToken,null,null);
             if(response.isOK()){
                 putRet= JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                 Map<String,String> uploadRes = new HashMap<>();
                 uploadRes.put("filePath",putRet.key);
                 uploadRes.put("fileFullPath",prefix+putRet.key);
                 uploadRes.put("group",bucket);
                 uploadRes.put("path",putRet.key);
                 uploadRes.put("fileName",file.getOriginalFilename());
                 return ApiResult.result(uploadRes);
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResult.result(putRet);
    }

    @Override
    public ApiResult keepUploadFile(Chunk chunk) {
        Map<String,Object> params=new HashMap<>();
        MultipartFile multipartFile=chunk.getFile();
        DefaultPutRet putRet=null;
        try {
            FileRecorder fileRecorder = new FileRecorder(byteCmsConfig.getSourceRootPath());
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(multipartFile.getInputStream(), chunk.getIdentifier(), upToken,null,null);
            if(response.isOK()){
                putRet= JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                params.put("filePath",putRet.key);
                params.put("fileFullPath",endpoint+putRet.key);
                params.put("group",bucket);
                params.put("path",putRet.key);
                params.put("fileName",multipartFile.getOriginalFilename());
                params.put("finshed",true);
                params.put("fileMd5",chunk.getIdentifier());
            }
            //解析上传成功的结果
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ApiResult.result(params);
    }

    @Override
    public ApiResult readyUpload(MultipartFile file) {
        if(Checker.BeNotNull(file)){
            return  ApiResult.result();
        }
        return  ApiResult.result(20026);
    }

    @Override
    public ApiResult uploadSuccess(ApiResult result, MultipartFile file) {
        return saveFileToDb(result,file);
    }

    @Override
    public void uploadError(ApiResult result) {

    }

    @Override
    public ApiResult deleteFile(Map<String, String> params) {
        String uid=params.get("fileUid");
        SysResourceDto resourceDto=getFileByUid(uid);
        if(Checker.BeNotNull(resourceDto)){
            String filePath=resourceDto.getFilePath();
            String groupName=resourceDto.getGroupName();
            if(Checker.BeNotBlank(filePath)){
                try {
                    bucketManager.delete(groupName,filePath);
                } catch (QiniuException e) {
                    e.printStackTrace();
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
            params.put("path",resource.getFilePath());
            params.put("id",resource.getId());
            params.put("skip",true);
        }else{
            params.put("skip",false);
            String fileKey= Constants.fastDfsKeepUpload+md5;
            List<Integer> uploadChunk = ( List<Integer>)baseRedisService.get(fileKey);
            params.put("uploadChunk",Checker.BeNotEmpty(uploadChunk)?uploadChunk: Lists.newArrayList());
        }
        return params;
    }
}
