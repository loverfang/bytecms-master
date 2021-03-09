package net.bytecms.service.service.upload;

import net.bytecms.core.config.ThinkCmsConfig;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.BaseContextKit;
import net.bytecms.core.utils.FileUtil;
import net.bytecms.core.utils.SnowflakeIdWorker;
import net.bytecms.service.api.resource.SysResourceService;
import net.bytecms.service.dto.resource.SysResourceDto;
import net.bytecms.service.dto.upload.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

public abstract class UploadClient {

    @Autowired
    SysResourceService resourceService;

    @Autowired
    ThinkCmsConfig thinkCmsConfig;

    protected String accessKey;

    protected String secretKey;

    protected String endpoint;

    protected String bucket;

    protected String prefix;


    /**
     * 文件上传中触发
     * @param file
     * @return
     */
    public abstract ApiResult uploadFile(MultipartFile file);


    /**
     * 断点续传
     * @param chunk
     * @return
     */
    public abstract ApiResult keepUploadFile(Chunk chunk);

    /**
     * 文件上传前触发
     * @param file
     * @return
     */
    public abstract ApiResult readyUpload(MultipartFile file);

    /**
     * 文件上传成功触发
     * @param file
     * @return
     */
    public  abstract ApiResult uploadSuccess(ApiResult apiResult,MultipartFile file);

    /**
     * 文件上传失败触发
     * @param result
     * @return
     */
    public abstract void uploadError(ApiResult result);

    /**
     * 删除文件
     * @param params
     * @return
     */
    public abstract ApiResult deleteFile(Map<String,String> params);


    /**
     * 校验 文件是否存在
     * @param md5
     * @return
     */
    public abstract Map<String,Object>  checkFileIsExistByMd5(String md5);


    public ApiResult saveFileToDb(ApiResult resMap,MultipartFile file){
            Map<String,String> params= (Map<String, String>) resMap.get("res");
            String id = SnowflakeIdWorker.getId();
            String uid=UUID.randomUUID().toString();
            SysResourceDto resource = new SysResourceDto();
            String path = params.get("path");
            String group = params.get("group");
            String fileMd5=params.get("fileMd5");
            resource.setFileName(file.getOriginalFilename()).setFilePath(path).
            setFileFullPath(params.get("fileFullPath")).setGroupName(group)
            .setId(id).setFileSize(file.getSize()).setFileUid(uid).setFileMd5(fileMd5).
            setFileType(FileUtil.getSuffix(file.getOriginalFilename())).setCreateName(BaseContextKit.getName());
            resourceService.insert(resource);
            params.put("fileUid",uid);
            params.put("id",id);
            return ApiResult.result(params);
    }

    public ApiResult deleteFileByUid(String uid){
           resourceService.deleteByFiled("file_uid",uid);
           return  ApiResult.result();
    }

    public SysResourceDto getFileByUid(String uuid){
        return resourceService.getByField("file_uid",uuid);
    }

    public SysResourceDto checkerHasFileByMd5(String md5){
         SysResourceDto resource= resourceService.getByField("file_md5",md5);
         return resource;
    }


}
