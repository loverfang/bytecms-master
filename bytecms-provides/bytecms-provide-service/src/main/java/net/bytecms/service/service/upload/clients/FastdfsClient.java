package net.bytecms.service.service.upload.clients;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.collect.Lists;
import net.bytecms.core.annotation.DefaultUploadClient;
import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.config.ByteCmsConfig;
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

import java.io.File;
import java.util.*;
@Component
@DefaultUploadClient
public class FastdfsClient extends UploadClient {

    @Autowired
    BaseRedisService baseRedisService;

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    AppendFileStorageClient appendFileStorageClient;

    @Autowired
    private ByteCmsConfig byteCmsConfig;

    @Override
    public ApiResult uploadFile(MultipartFile multipartFile) {
        try {
            StorePath storePath=storageClient.uploadFile(Constants.FDFS_GROUP,multipartFile.getInputStream(),
            multipartFile.getSize(), FileUtil.getSuffix(multipartFile.getOriginalFilename()));
            if(Checker.BeNotNull(storePath) && Checker.BeNotBlank(storePath.getPath())){
                Map<String,String> uploadRes = new HashMap<>();
                uploadRes.put("filePath",storePath.getFullPath());
                uploadRes.put("fileFullPath", byteCmsConfig.getSiteFdfsDomain()+ File.separator+storePath.getFullPath());
                uploadRes.put("group",storePath.getGroup());
                uploadRes.put("path",storePath.getPath());
                uploadRes.put("fileName",multipartFile.getOriginalFilename());
                return ApiResult.result(uploadRes);
            }
            return  ApiResult.result(20004);
        } catch (Exception e) {
            return  ApiResult.result(20004);
        }
    }

    @Override
    public ApiResult keepUploadFile(Chunk chunk) {
        Map<String,Object> param=new HashMap<>();
        MultipartFile multipartFile=chunk.getFile();
        String fileKey=Constants.fastDfsKeepUpload+chunk.getIdentifier();
        String pathKey=Constants.fastDfsKeepUpload+chunk.getIdentifier()+"_path";
        try {
            if(!checkIsUpload(chunk)){
                StorePath storePath=null;
                Map<String,Object> params=null;
                boolean isLastChunk=chunk.getChunkNumber().intValue()==chunk.getTotalChunks().intValue();
                if(chunk.getChunkNumber()==1){
                    storePath=appendFileStorageClient.uploadAppenderFile(Constants.FDFS_GROUP,multipartFile.getInputStream(),
                    multipartFile.getSize(), FileUtil.getSuffix(multipartFile.getOriginalFilename()));
                    if(isLastChunk){
                        params=new HashMap<>(16);
                        params.put("filePath",storePath.getFullPath());
                        params.put("fileFullPath", byteCmsConfig.getSiteFdfsDomain()+ File.separator+storePath.getFullPath());
                        params.put("group",storePath.getGroup());
                        params.put("path",storePath.getPath());
                    }
                }else{
                    //获取 path
                    params  =(Map<String, Object>) baseRedisService.get(pathKey);
                    appendFileStorageClient.appendFile(Constants.FDFS_GROUP,params.get("path").toString(),multipartFile.getInputStream(),
                    multipartFile.getSize());
                }

                markUpload(chunk,isLastChunk,storePath);
                if(isLastChunk){
                    param.put("finshed",isLastChunk);
                    param.put("fileMd5",chunk.getIdentifier());
                    param.put("fileName",multipartFile.getOriginalFilename());
                    param.putAll(params);
                }
            }
            return ApiResult.result(param);
        }catch (Exception e){
            // baseRedisService.removeByKeys(Arrays.asList(fileKey,pathKey));
            return  ApiResult.result(20004);
        }
    }

    private boolean checkIsUpload(Chunk chunk){
       String fileKey=Constants.fastDfsKeepUpload+chunk.getIdentifier();
       List<Integer> uploadChunk = ( List<Integer>)baseRedisService.get(fileKey);
       if(Checker.BeNotEmpty(uploadChunk)){
            Integer curetChunk=chunk.getChunkNumber();
            return uploadChunk.contains(curetChunk);
       }else{
           return false;
       }
    }

    private boolean markUpload(Chunk chunk,boolean isLastChunk,StorePath storePath){
        String fileKey=Constants.fastDfsKeepUpload+chunk.getIdentifier();
        String pathKey=Constants.fastDfsKeepUpload+chunk.getIdentifier()+"_path";
        if(chunk.getChunkNumber()==1 && Checker.BeNotNull(storePath)){
            Map<String,String> params=new HashMap<>(16);
            params.put("filePath",storePath.getFullPath());
            params.put("fileFullPath", byteCmsConfig.getSiteFdfsDomain()+ File.separator+storePath.getFullPath());
            params.put("group",storePath.getGroup());
            params.put("path",storePath.getPath());
            baseRedisService.set(pathKey,params);
        }
        if(isLastChunk){
            baseRedisService.removeByKeys(Arrays.asList(fileKey,pathKey));
        }else{
            List<Integer> uploadChunk = ( List<Integer>)baseRedisService.get(fileKey);
            if(Checker.BeEmpty(uploadChunk)){
                uploadChunk=new ArrayList<>();
            }
            uploadChunk.add(chunk.getChunkNumber());
            baseRedisService.set(fileKey,uploadChunk);
        }
        return true;
    }

    @Override
    public ApiResult readyUpload(MultipartFile file) {
        if(Checker.BeNotNull(file)){
            return  ApiResult.result();
        }
        return  ApiResult.result(20026);
    }

    @Override
    public ApiResult uploadSuccess(ApiResult result,MultipartFile file) {
        return saveFileToDb(result,file);
    }

    @Override
    public void uploadError(ApiResult result) {

    }

    @Override
    public ApiResult deleteFile(Map<String,String> params) {
        String uid=params.get("fileUid");
        SysResourceDto resourceDto=getFileByUid(uid);
        if(Checker.BeNotNull(resourceDto)){
            String filePath=resourceDto.getFilePath();
            String groupName=resourceDto.getGroupName();
            if(Checker.BeNotBlank(filePath)){
                storageClient.deleteFile(groupName,filePath);
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
            params.put("id",resource.getId());
            params.put("group",resource.getGroupName());
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
}
