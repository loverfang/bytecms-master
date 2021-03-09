package net.bytecms.web.controller.fragment;

import net.bytecms.core.annotation.Logs;
import net.bytecms.core.enumerate.LogModule;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.TreeFileInfo;
import net.bytecms.service.api.fragment.FragmentFileModelService;
import net.bytecms.service.dto.fragment.FragmentDirectoryDto;
import net.bytecms.service.dto.fragment.FragmentFileModelDto;
import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.service.dto.webfile.FileContentDto;
import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 页面片段文件模型 前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
@Validated
@RestController
@RequestMapping("fragmentFileModel")
public class FragmentFileModelController extends BaseController<FragmentFileModelService> {


    @Logs(module = LogModule.FRAGMENT,operation = "获取页面片段详情")
    @GetMapping("getByPk")
    public FragmentFileModelDto get(@NotBlank @RequestParam String id){
       return  service.getInfoByPk(id);
    }


    @Logs(module = LogModule.FRAGMENT,operation = "获取页面片段设计详情")
    @GetMapping("getFragmentDesignByPk")
    public FragmentFileModelDto getFragmentDesignByPk(@NotBlank @RequestParam String id){
        return  service.getFragmentDesignByPk(id);
    }

    @PostMapping(value="save")
    public void save(@Validated @RequestBody FragmentFileModelDto v){
        service.insert(v);
    }

    @PutMapping("update")
    public void update(@RequestBody FragmentFileModelDto v){
        service.updateByPk(v);
    }

    @DeleteMapping("deleteByPk")
    public boolean deleteByPk(@NotBlank @RequestParam String pk) {
         return service.deleteByPk(pk);
    }

    @DeleteMapping(value = "deleteByIds")
    public void deleteByPks(@NotEmpty @RequestBody List<String> ids){
          service.deleteByPks(ids);
    }

    @PostMapping("list")
    public  List<FragmentFileModelDto> list(@RequestBody FragmentFileModelDto v){
        return service.listDto(v);
    }

    @PostMapping("page")
    public PageDto<FragmentFileModelDto> listPage(@RequestBody PageDto<FragmentFileModelDto> pageDto){
        return service.listPage(pageDto);
    }

    @GetMapping(value = "/treeFragmentFiles")
    public TreeFileInfo list(){
        return  service.treeFragmentFiles();
    }


    @GetMapping(value = "/getFileContent")
    public ApiResult getFileContent(@NotBlank String path){
        return  service.getFileContent(path);
    }


    @Logs(module = LogModule.FRAGMENT,operation = "更新页面片段文件内容")
    @PostMapping(value = "/setFileContent")
    public ApiResult setFileContent(@RequestBody FileContentDto fileContentDto ){
        return  service.setFileContent(fileContentDto.getFilePath(),fileContentDto.getFileContent());
    }

    @Logs(module = LogModule.FRAGMENT,operation = "删除页面片段文件")
    @DeleteMapping(value = "/deleteFile")
    public ApiResult deleteFile(@RequestParam("path") String filePath){
        return  service.deleteFile(filePath);
    }

    @Logs(module = LogModule.FRAGMENT,operation = "创建页面片段文件")
    @PostMapping(value="saveFragmentFile")
    public void saveFragmentFile(@Validated @RequestBody FragmentFileModelDto v){
        service.saveFragmentFile(v);
    }

    @Logs(module = LogModule.FRAGMENT,operation = "修改页面片段文件设计")
    @PostMapping(value="updateFragmentDesignFile")
    public void updateFragmentFile(@Validated @RequestBody FragmentFileModelDto v){
        service.updateFragmentDesignFile(v);
    }

    @Logs(module = LogModule.FRAGMENT,operation = "创建页面片段目录")
    @PostMapping(value="saveFragmentDirectory")
    public void saveFragmentDirectory(@Validated @RequestBody FragmentDirectoryDto v){
        service.saveFragmentDirectory(v);
    }

    @GetMapping("listDefaultField")
    public List<CmsDefaultModelFieldDto> listSysFields(){
        return  service.listDefaultField();
    }

    @PostMapping("downZip")
    public void downZip(String path,HttpServletResponse response){
          service.downZip(path,response);
    }

}
