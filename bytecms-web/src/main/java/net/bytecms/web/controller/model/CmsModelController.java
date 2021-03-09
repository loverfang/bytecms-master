package net.bytecms.web.controller.model;
import net.bytecms.service.api.model.CmsModelService;
import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.service.dto.model.CmsModelDto;
import net.bytecms.core.annotation.Logs;
import net.bytecms.core.enumerate.LogModule;
import net.bytecms.core.model.PageDto;
import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-10-23
 */
@Validated
@RestController
@RequestMapping("cmsModel")
public class CmsModelController extends BaseController<CmsModelService> {

    @Logs(module = LogModule.MODEL,operation = "查看模型详情")
    @RequestMapping(value="getById",method = RequestMethod.GET)
    public CmsModelDto getById(@RequestParam String id){
       return service.getByPk(id);
    }

    @Logs(module = LogModule.MODEL,operation = "创建模型")
    @RequestMapping(value="save",method = RequestMethod.POST)
    public void save(@RequestBody CmsModelDto v){
         service.insert(v);
    }

    @Logs(module = LogModule.MODEL,operation = "更新模型")
    @RequestMapping(value="update",method = RequestMethod.PUT)
    public void update(@RequestBody CmsModelDto v){
        service.updateByPk(v);
    }

    @Logs(module = LogModule.MODEL,operation = "删除模型")
    @RequestMapping(value="delete",method = RequestMethod.DELETE)
    public void delete(@NotBlank String id){
        service.deleteByPk(id);
    }

    @RequestMapping(value = "deleteByIds",method = RequestMethod.DELETE)
    public void delete(@RequestBody List<String> ids){
        service.deleteByPks(ids);
    }

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public  List<CmsModelDto> list(@RequestBody CmsModelDto v){
        return  service.listDto(v);
    }


    @RequestMapping(value = "/listAllModel",method = RequestMethod.POST)
    public  List<CmsModelDto> listAllModel(@RequestBody CmsModelDto v){
        //v.condition().select("id","name","template_path");
        return  service.listModelByCategory(v);
    }

    @Logs(module = LogModule.MODEL,operation = "查看模型列表")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public PageDto<CmsModelDto> listPage(@RequestBody PageDto<CmsModelDto> dto){
        return service.listPage(dto);
    }

    @GetMapping("listDefaultField")
    public List<CmsDefaultModelFieldDto> listSysFields(){
        return  service.listDefaultField();
    }

}
