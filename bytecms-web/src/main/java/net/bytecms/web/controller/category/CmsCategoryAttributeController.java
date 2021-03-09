package net.bytecms.web.controller.category;
import java.util.List;

import net.bytecms.service.api.category.CmsCategoryAttributeService;
import net.bytecms.service.dto.category.CmsCategoryAttributeDto;
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
@RequestMapping("cmsCategoryAttribute")
public class CmsCategoryAttributeController extends BaseController<CmsCategoryAttributeService> {


    @GetMapping("getByPk")
    public CmsCategoryAttributeDto get(@NotBlank @RequestParam String id){
       return  service.getByPk(id);
    }

    @PostMapping(value="save")
    public void save(@Validated @RequestBody CmsCategoryAttributeDto v){
        service.insert(v);
    }

    @PutMapping("update")
    public void update(@RequestBody CmsCategoryAttributeDto v){
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
    public  List<CmsCategoryAttributeDto> list(@RequestBody CmsCategoryAttributeDto v){
        return service.listDto(v);
    }

    @PostMapping("page")
    public PageDto<CmsCategoryAttributeDto> listPage(@RequestBody PageDto<CmsCategoryAttributeDto> pageDto){
        return service.listPage(pageDto);
    }

}
