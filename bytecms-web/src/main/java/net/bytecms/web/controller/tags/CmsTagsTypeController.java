package net.bytecms.web.controller.tags;
import java.util.List;

import net.bytecms.service.api.tags.CmsTagsTypeService;
import net.bytecms.service.dto.tags.CmsTagsTypeDto;
import net.bytecms.core.annotation.Logs;
import net.bytecms.core.enumerate.LogModule;
import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import net.bytecms.core.model.PageDto;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 标签类型 前端控制器
 * </p>
 *
 * @author LG
 * @since 2020-01-31
 */
@Validated
@RestController
@RequestMapping("cmsTagsType")
public class CmsTagsTypeController extends BaseController<CmsTagsTypeService> {


    @GetMapping("getByPk")
    public CmsTagsTypeDto get(@NotBlank @RequestParam String id){
       return  service.getByPk(id);
    }

    @Logs(module = LogModule.TAG,operation = "创建标签分类")
    @PostMapping(value="save")
    public void save(@Validated @RequestBody CmsTagsTypeDto v){
        service.save(v);
    }


    @Logs(module = LogModule.TAG,operation = "更新标签分类")
    @PutMapping("update")
    public void update(@RequestBody CmsTagsTypeDto v){
        service.update(v);
    }


    @Logs(module = LogModule.TAG,operation = "删除标签分类")
    @DeleteMapping("deleteByPk")
    public boolean deleteByPk(@NotBlank @RequestParam String id) {
         return service.deleteById(id);
    }

    @DeleteMapping(value = "deleteByIds")
    public void deleteByPks(@NotEmpty @RequestBody List<String> ids){
          service.deleteByPks(ids);
    }

    @PostMapping("list")
    public  List<CmsTagsTypeDto> list(@RequestBody CmsTagsTypeDto v){
        return service.listDto(v);
    }


    @Logs(module = LogModule.TAG,operation = "查看标签分类列表页")
    @PostMapping("page")
    public PageDto<CmsTagsTypeDto> listPage(@RequestBody PageDto<CmsTagsTypeDto> pageDto){
        return service.listPage(pageDto);
    }

    @Logs(module = LogModule.TAG,operation = "归属标签")
    @PutMapping("tagsBelong")
    public void tagsBelong(@RequestBody CmsTagsTypeDto v){
        service.tagsBelong(v);
    }

}
