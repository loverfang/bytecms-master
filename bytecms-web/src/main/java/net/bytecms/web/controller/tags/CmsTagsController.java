package net.bytecms.web.controller.tags;
import java.util.ArrayList;
import java.util.List;

import net.bytecms.service.api.tags.CmsTagsService;
import net.bytecms.service.dto.tags.CmsTagsDto;
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
 * 标签 前端控制器
 * </p>
 *
 * @author LG
 * @since 2020-01-31
 */
@Validated
@RestController
@RequestMapping("cmsTags")
public class CmsTagsController extends BaseController<CmsTagsService> {


    @Logs(module = LogModule.TAG,operation = "获取标签详情")
    @GetMapping("getByPk")
    public CmsTagsDto get(@NotBlank @RequestParam String id){
       return  service.getByPk(id);
    }

    @Logs(module = LogModule.TAG,operation = "创建标签")
    @PostMapping(value="save")
    public void save(@Validated @RequestBody CmsTagsDto v){
        List<String> tags=new ArrayList<>(16);
        tags.add(v.getName());
        service.saveTags(tags);
    }

    @Logs(module = LogModule.TAG,operation = "更新标签")
    @PutMapping("update")
    public void update(@RequestBody CmsTagsDto v){
        service.update(v);
    }


    @Logs(module = LogModule.TAG,operation = "删除标签")
    @DeleteMapping("deleteByPk")
    public boolean deleteByPk(@NotBlank @RequestParam String id) {
         return service.deleteByPk(id);
    }

    @DeleteMapping(value = "deleteByIds")
    public void deleteByPks(@NotEmpty @RequestBody List<String> ids){
          service.deleteByPks(ids);
    }

    @PostMapping("list")
    public  List<CmsTagsDto> list(@RequestBody CmsTagsDto v){
        return service.listDto(v);
    }


    @Logs(module = LogModule.TAG,operation = "查询标签列表页")
    @PostMapping("page")
    public PageDto<CmsTagsDto> listPage(@RequestBody PageDto<CmsTagsDto> pageDto){
        return service.listPage(pageDto);
    }

}
