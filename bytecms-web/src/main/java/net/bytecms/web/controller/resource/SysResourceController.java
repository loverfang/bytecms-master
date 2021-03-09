package net.bytecms.web.controller.resource;
import net.bytecms.core.model.PageDto;
import net.bytecms.service.api.resource.SysResourceService;
import net.bytecms.service.dto.resource.SysResourceDto;
import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-11-11
 */
@Validated
@RestController
@RequestMapping("resource")
public class SysResourceController extends BaseController<SysResourceService> {
    @GetMapping("getByPk")
    public SysResourceDto get(@NotBlank @RequestParam String id){
       return  service.getByPk(id);
    }

    @PostMapping(value="save")
    public void save(@Validated @RequestBody SysResourceDto v){
        service.insert(v);
    }

    @PutMapping("update")
    public void update(@RequestBody SysResourceDto v){
        service.updateByPk(v);
    }

    @DeleteMapping("deleteByPk")
    public boolean deleteByPk(@NotBlank @RequestParam String id) {
         return service.deleteByPk(id);
    }

    @DeleteMapping(value = "deleteByIds")
    public void deleteByPks(@NotEmpty @RequestBody List<String> ids){
          service.deleteByPks(ids);
    }

    @PostMapping("list")
    public  List<SysResourceDto> list(@RequestBody SysResourceDto v){
        return service.listDto(v);
    }

    @PostMapping("page")
    public PageDto<SysResourceDto> listPage(@RequestBody PageDto<SysResourceDto> pageDto){
        return service.listPage(pageDto);
    }

}
