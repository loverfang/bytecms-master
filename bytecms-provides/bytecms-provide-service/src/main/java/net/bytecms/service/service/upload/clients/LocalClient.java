package net.bytecms.service.service.upload.clients;

import cn.hutool.core.date.DateUtil;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.collect.Lists;
import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.config.ThinkCmsConfig;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.FileUtil;
import net.bytecms.core.utils.SnowflakeIdWorker;
import net.bytecms.service.dto.resource.SysResourceDto;
import net.bytecms.service.dto.upload.Chunk;
import net.bytecms.service.service.upload.UploadClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Component
public class LocalClient extends UploadClient {

    @Autowired
    BaseRedisService baseRedisService;

    @Autowired
    private FastFileStorageClient storageClient;

    private Map<String,String> contentMap=new HashMap<>(16);

    @Autowired
    AppendFileStorageClient appendFileStorageClient;

    @Autowired
    private ThinkCmsConfig thinkCmsConfig;



    @Override
    public ApiResult uploadFile(MultipartFile multipartFile) {
        try {
            String root=thinkCmsConfig.getFileResourcePath();
            String fileName = getFileName(multipartFile);
            FileUtil.upload(multipartFile.getBytes(),root+getFilePath(multipartFile),root+fileName,false);
            Map<String,Object> uploadRes = new HashMap<>();
            uploadRes.put("filePath",fileName);
            uploadRes.put("fileFullPath", thinkCmsConfig.getServerApi()+Constants.localtionUploadPattern+fileName);
            uploadRes.put("group",getGroup(multipartFile));
            uploadRes.put("path",fileName);
            uploadRes.put("fileName",multipartFile.getOriginalFilename());
            uploadRes.put("finshed",true);
            return ApiResult.result(uploadRes);
        } catch (Exception e) {
            return  ApiResult.result(20004);
        }
    }

    @Override
    public ApiResult keepUploadFile(Chunk chunk) {
        Map<String,Object> params=new HashMap<>();
        MultipartFile multipartFile=chunk.getFile();
        String root=thinkCmsConfig.getFileResourcePath();
        try {
            if(!checkIsUpload(chunk)){
                String fileName = getFileName(multipartFile);
                boolean isLastChunk=chunk.getChunkNumber().intValue()==chunk.getTotalChunks().intValue();
                if(chunk.getTotalChunks().intValue()==1){
                    FileUtil.upload(multipartFile.getBytes(),root+getFilePath(multipartFile),root+fileName,false);
                    params.put("fileMd5",chunk.getIdentifier());
                    params.put("fileName",multipartFile.getOriginalFilename());
                    params.put("filePath",fileName);
                    params.put("path",fileName);
                    params.put("fileFullPath", thinkCmsConfig.getServerApi()+Constants.localtionUploadPattern+fileName);
                    params.put("group",getGroup(multipartFile));
                    params.put("finshed",true);
                }else{
                    String pathKey=Constants.fastDfsKeepUpload+chunk.getIdentifier()+"_path";
                    if(chunk.getChunkNumber()==1){
                        FileUtil.upload(multipartFile.getBytes(),root+getFilePath(multipartFile),root+fileName,false);
                        markUpload(chunk,isLastChunk);
                        baseRedisService.set(pathKey,fileName);
                    }else{
                        Object fileNameObj=baseRedisService.get(pathKey);
                        if(Checker.BeNotNull(fileNameObj)){
                            FileUtil.uploadAppend(root,fileNameObj.toString(),multipartFile.getBytes());
                            markUpload(chunk,isLastChunk);
                            params.put("finshed",isLastChunk);
                            if(isLastChunk){
                                params.put("fileMd5",chunk.getIdentifier());
                                params.put("fileName",multipartFile.getOriginalFilename());
                                params.put("filePath",fileNameObj.toString());
                                params.put("path",fileNameObj.toString());
                                params.put("fileFullPath", thinkCmsConfig.getServerApi()+Constants.localtionUploadPattern+fileNameObj.toString());
                                params.put("group",getGroup(multipartFile));
                            }
                        }
                    }
                }
            }
            return ApiResult.result(params);
        }catch (Exception e){
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

    private boolean markUpload(Chunk chunk,boolean isLastChunk){
        String fileKey=Constants.fastDfsKeepUpload+chunk.getIdentifier();
        String pathKey=Constants.fastDfsKeepUpload+chunk.getIdentifier()+"_path";
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
            if(Checker.BeNotBlank(filePath)){
                filePath=thinkCmsConfig.getFileResourcePath()+File.separator+filePath;
                cn.hutool.core.io.FileUtil.del(filePath);
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


    private String getContentMap(String contentType){
        if(contentMap.isEmpty()){
            contentMap.put("text/html","html");
            contentMap.put("ext/plain","text");
            contentMap.put("text/xml","xml");
            contentMap.put("image/gif","image");
            contentMap.put("image/jpeg","image");
            contentMap.put("image/png","image");
            contentMap.put("application/xhtml+xml","xml");
            contentMap.put("application/pdf","document");
            contentMap.put("application/msword","document");
            contentMap.put("application/octet-stream","file");
        }
        String type=contentMap.get(contentType);
        return Checker.BeNotBlank(type)?type:"file";
    }

    private String getGroup(MultipartFile multipartFile){
        return getContentMap(multipartFile.getContentType());
    }

    private String getFilePath(MultipartFile multipartFile){
        String filePath = getGroup(multipartFile)+File.separator+
                DateUtil.format(new Date(), "yyyyMMdd") +File.separator;
        return filePath;
    }

    private String getFileName(MultipartFile multipartFile){
        return getFilePath(multipartFile)+ SnowflakeIdWorker.getId()+Constants.DOT+
                FileUtil.getSuffix(multipartFile.getOriginalFilename());
    }
}
