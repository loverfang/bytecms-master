package ${package.Controller};
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import ${package.Service}.${cfg.customApiName?cap_first};
import ${cfg.customDtoPack}.${cfg.customDtoName?cap_first};
import PageDto;
import org.springframework.web.bind.annotation.*;
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Validated
@RestController
@RequestMapping("api/${cfg.customModelName?uncap_first}")
public class ${cfg.customControllerName?cap_first} extends ${superControllerClass}<${cfg.customApiName?cap_first}> {


    @GetMapping("getByPk")
    public ${cfg.customDtoName?cap_first} get(@NotBlank @RequestParam String id){
       return  service.getByPk(id);
    }

    @PostMapping(value="save")
    public void save(@Validated @RequestBody ${cfg.customDtoName?cap_first} v){
        service.insert(v);
    }

    @PutMapping("update")
    public void update(@RequestBody ${cfg.customDtoName?cap_first} v){
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
    public  List<${cfg.customDtoName?cap_first}> list(@RequestBody ${cfg.customDtoName?cap_first} v){
        return service.listDto(v);
    }

    @PostMapping("page")
    public PageDto<${cfg.customDtoName?cap_first}> listPage(@RequestBody PageDto<${cfg.customDtoName?cap_first}> pageDto){
        return service.listPage(pageDto);
    }

}
