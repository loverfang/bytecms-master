package net.bytecms.web.controller.resource;
import net.bytecms.service.api.upload.UploadService;
import net.bytecms.service.dto.upload.Chunk;
import net.bytecms.core.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("upload")
public class UploadController  {

    @Autowired
    UploadService uploadService;

    @PostMapping("uploadFile")
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file){
        return uploadService.uploadFile(file);
    }

    @PostMapping("keepUploadFile")
    public ApiResult keepUploadFile(Chunk chunk){
        return uploadService.keepUploadFile(chunk);
    }


    @GetMapping("keepUploadFile")
    public ApiResult checkFileIsExist(Chunk chunk){
        return uploadService.checkFileIsExist(chunk);
    }


    @DeleteMapping("deleteFile")
    public ApiResult deleteFile(@RequestParam Map<String,String> params){
        return uploadService.deleteFile(params);
    }

}
