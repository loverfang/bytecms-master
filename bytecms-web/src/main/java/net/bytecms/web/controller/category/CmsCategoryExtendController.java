package net.bytecms.web.controller.category;
import java.util.List;

import net.bytecms.service.api.category.CmsCategoryExtendService;
import net.bytecms.service.dto.category.CmsCategoryExtendDto;
import net.bytecms.core.annotation.Logs;
import net.bytecms.core.enumerate.LogModule;
import net.bytecms.core.enumerate.LogOperation;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import net.bytecms.core.model.PageDto;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 分类扩展 前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
@Validated
@RestController
@RequestMapping("cmsCategoryExtend")
public class CmsCategoryExtendController extends BaseController<CmsCategoryExtendService> {


    @GetMapping("getByPk")
    public CmsCategoryExtendDto get(@NotBlank @RequestParam String id){
       return  service.getByPk(id);
    }

    @Logs(module = LogModule.CATEGORY,operaEnum = LogOperation.SAVE)
    @PostMapping(value="save")
    public void save(@Validated @RequestBody CmsCategoryExtendDto v){
        service.insert(v);
    }

    @Logs(module = LogModule.CATEGORY,operaEnum = LogOperation.UPDATE)
    @PutMapping("update")
    public void update(@RequestBody CmsCategoryExtendDto v){
        service.updateByPk(v);
    }

    @Logs(module = LogModule.CATEGORY,operaEnum = LogOperation.DELETE)
    @DeleteMapping("deleteByPk")
    public boolean deleteByPk(@NotBlank @RequestParam String pk) {
         return service.deleteByPk(pk);
    }

    @DeleteMapping(value = "deleteByIds")
    public void deleteByPks(@NotEmpty @RequestBody List<String> ids){
          service.deleteByPks(ids);
    }

    @PostMapping("list")
    public  List<CmsCategoryExtendDto> list(@RequestBody CmsCategoryExtendDto v){
        v.condition().select("id","category_extend_name");
        return service.listDto(v);
    }

    @GetMapping("getExtendFieldById")
    public ApiResult getExtendFieldById(@NotBlank @RequestParam String id){
        return service.getExtendFieldById(id);
    }

    @PostMapping("page")
    public PageDto<CmsCategoryExtendDto> listPage(@RequestBody PageDto<CmsCategoryExtendDto> pageDto){
        return service.listPage(pageDto);
    }

}
