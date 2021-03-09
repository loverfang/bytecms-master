package net.bytecms.web.controller.webfile;

import net.bytecms.core.annotation.Logs;
import net.bytecms.core.enumerate.LogModule;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.TreeFileInfo;
import net.bytecms.service.api.webfile.TemplateService;
import net.bytecms.service.dto.webfile.FileContentDto;
import net.bytecms.service.dto.webfile.TemplateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("templates")
public class TemplateController  {

    @Autowired
    TemplateService templateService;

    @GetMapping(value = "/treeTemps")
    public TreeFileInfo list(){
        return  templateService.treeTempFile();
    }

    @GetMapping(value = "/getFileContent")
    public ApiResult getFileContent(@NotBlank String path){
        return  templateService.getFileContent(path);
    }

    @GetMapping(value = "/getDirects")
    public ApiResult getDirects(){
        return  templateService.getDirects();
    }

    @Logs(module = LogModule.TEMPLATES,operation = "修改保存模板文件")
    @PostMapping(value = "/setFileContent")
    public ApiResult setFileContent(@RequestBody FileContentDto fileContentDto ){
        return  templateService.setFileContent(fileContentDto.getFilePath(),fileContentDto.getFileContent());
    }


    @Logs(module = LogModule.TEMPLATES,operation = "创建模板文件")
    @PostMapping(value = "/saveTemp")
    public void saveTemp(@RequestBody TemplateDto templateDto){
        templateService.saveTemp(templateDto);
    }


    @Logs(module = LogModule.TEMPLATES,operation = "删除模板文件")
    @DeleteMapping(value = "/deleteFile")
    public ApiResult deleteFile(@RequestParam("path") String filePath){
        return  templateService.deleteFile(filePath);
    }


    @Logs(module = LogModule.TEMPLATES,operation = "下载模板文件")
    @PostMapping("downZip")
    public void downZip(String path,HttpServletResponse response){
        templateService.downZip(path,response);
    }


    @Logs(module = LogModule.TEMPLATES,operation = "导入模板文件")
    @PostMapping("importFile")
    public void importFile(MultipartFile file,Integer type){
        templateService.importFile(file,type);
    }

}
