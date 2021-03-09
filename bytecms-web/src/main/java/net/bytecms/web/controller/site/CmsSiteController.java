package net.bytecms.web.controller.site;
import java.util.List;

import net.bytecms.service.api.site.CmsSiteService;
import net.bytecms.service.dto.site.CmsSiteDto;
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
 *  前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-12-11
 */
@Validated
@RestController
@RequestMapping("cmsSite")
public class CmsSiteController extends BaseController<CmsSiteService> {


    @Logs(module = LogModule.SITE,operation = "查看站点配置详情")
    @GetMapping("getByPk")
    public CmsSiteDto get(@NotBlank @RequestParam String id){
       return  service.getByPk(id);
    }

    @Logs(module = LogModule.SITE,operation = "创建站点配置")
    @PostMapping(value="save")
    public void save(@Validated @RequestBody CmsSiteDto v){
        service.save(v);
    }


    @Logs(module = LogModule.SITE,operation = "更新站点配置")
    @PutMapping("update")
    public void update(@RequestBody CmsSiteDto v){
        service.updateById(v);
    }


    @Logs(module = LogModule.SITE,operation = "删除站点配置")
    @DeleteMapping("deleteByPk")
    public boolean deleteByPk(@NotBlank @RequestParam String pk) {
         return service.deleteById(pk);
    }

    @DeleteMapping(value = "deleteByIds")
    public void deleteByPks(@NotEmpty @RequestBody List<String> ids){
          service.deleteByPks(ids);
    }

    @PostMapping("list")
    public  List<CmsSiteDto> list(@RequestBody CmsSiteDto v){
        return service.listDto(v);
    }


    @Logs(module = LogModule.SITE,operation = "查看站点配置列表")
    @PostMapping("page")
    public PageDto<CmsSiteDto> listPage(@RequestBody PageDto<CmsSiteDto> pageDto){
        return service.listPage(pageDto);
    }

}
