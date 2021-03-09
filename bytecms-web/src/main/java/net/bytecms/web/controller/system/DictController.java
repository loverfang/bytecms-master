package net.bytecms.web.controller.system;
import net.bytecms.core.model.PageDto;
import net.bytecms.system.api.system.DictService;
import net.bytecms.system.dto.system.DictDto;
import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-08-29
 */
@Validated
@RestController
@RequestMapping("/dict")
public class DictController extends BaseController<DictService> {


    @RequestMapping(value="save",method = RequestMethod.POST)
    public void save(@Valid @RequestBody DictDto v){
        service.insert(v);
    }

    @RequestMapping(value="update",method = RequestMethod.PUT)
    public void update(@Valid @RequestBody DictDto v){
        service.updateByPk(v);
    }


    @RequestMapping(value = "deleteByIds",method = RequestMethod.DELETE)
    public void delete(@RequestBody List<String> ids){
        service.deleteByPks(ids);
    }

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public  List<DictDto> list(@RequestBody DictDto v){
        return service.listDto(v);
    }

    @RequestMapping(value = "/listType",method = RequestMethod.POST)
    public  List<DictDto> listType(@RequestBody DictDto v){
        return service.listType(v);
    }

    @RequestMapping(value = "/listByType",method = RequestMethod.GET)
    public  List<DictDto> listByType(@RequestParam String type){
        return service.listByType(type);
    }

    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public PageDto<DictDto> listPage(@RequestBody PageDto<DictDto> dto){
        dto.getDto().condition().orderByAsc("num").eq("type", dto.getDto().getType());
        return service.listPage(dto);
    }

}
