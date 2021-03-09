package net.bytecms.web.controller.webfile;

import net.bytecms.service.api.webfile.WebStaticFileService;
import net.bytecms.core.annotation.Logs;
import net.bytecms.core.enumerate.LogModule;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Validated
@RestController
@RequestMapping("webstatic")
public class WebStaticFileController {

    @Autowired
    WebStaticFileService wbStaticFileService;

    @Logs(module = LogModule.SITE_FILE,operation = "查看站点文件")
    @GetMapping("page")
    public List<FileInfo> page(
       @RequestParam(value = "path",required = false,defaultValue = "") String path,
       @RequestParam(value = "parentPath",required = false,defaultValue = "")  String parentPath) {
        return wbStaticFileService.listPage(path,parentPath);
    }


    @Logs(module = LogModule.SITE_FILE,operation = "删除站点文件")
    @DeleteMapping(value = "/deleteFile")
    public ApiResult deleteFile(@RequestParam("path") String filePath){
        return  wbStaticFileService.deleteFile(filePath);
    }


    @Logs(module = LogModule.SITE_FILE,operation = "压缩下载站点文件")
    @PostMapping("downZip")
    public void downZip(String path,HttpServletResponse response){
        wbStaticFileService.downZip(path,response);
    }


    @PostMapping("uploadFile")
    public ApiResult uploadFile(@RequestParam("files[]") List<MultipartFile> files,@RequestParam("filePath") String filePath){
        return wbStaticFileService.uploadFile(files,filePath);
    }


    @Logs(module = LogModule.SITE_FILE,operation = "创建站点目录")
    @PostMapping("createFile")
    public ApiResult createFile(@RequestParam(required = false,value = "filePath",defaultValue = "") String filePath,
                                @RequestParam(required = false,value = "fileName",defaultValue = "") String fileName){
        return wbStaticFileService.createFile(filePath,fileName);
    }

//
//    @GetMapping(value = "/getFileContent")
//    public ApiResult getFileContent(@NotBlank String path){
//        return  templateService.getFileContent(path);
//    }
//
//    @PutMapping(value = "/setFileContent")
//    public ApiResult setFileContent(@RequestParam String filePath,@RequestParam String fileContent){
//        return  templateService.setFileContent(filePath,fileContent);
//    }
//
//
//    @PostMapping(value = "/saveTemp")
//    public void saveTemp(@RequestBody TemplateDto templateDto){
//        templateService.saveTemp(templateDto);
//    }
//
//    @DeleteMapping(value = "/deleteFile")
//    public ApiResult deleteFile(@RequestParam("path") String filePath){
//        return  templateService.deleteFile(filePath);
//    }
//
//    @PostMapping("downZip")
//    public void downZip(String path,HttpServletResponse response){
//        templateService.downZip(path,response);
//    }
//
//    @PostMapping("importFile")
//    public void importFile(MultipartFile file,Integer type){
//        templateService.importFile(file,type);
//    }

}
