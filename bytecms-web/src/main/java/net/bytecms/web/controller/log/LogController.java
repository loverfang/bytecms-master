package net.bytecms.web.controller.log;

import net.bytecms.core.model.PageDto;
import net.bytecms.system.api.log.LogService;
import net.bytecms.system.dto.log.LogDTO;
import net.bytecms.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author lgs
 * @since 2019-08-12
 */
@RestController
@RequestMapping("log")
public class LogController extends BaseController<LogService> {


    @RequestMapping(value="get",method = RequestMethod.GET)
    public LogDTO get(@RequestParam String id){
       return  service.getByPk(id);
    }

    @RequestMapping(value="save",method = RequestMethod.POST)
    public void save(@RequestBody LogDTO v){
        service.insert(v);
    }

    @RequestMapping(value="update",method = RequestMethod.PUT)
    public void update(@RequestBody LogDTO v){
        service.updateByPk(v);
    }

    @RequestMapping(value="delete",method = RequestMethod.DELETE)
    public void delete(@RequestBody LogDTO v){
        service.deleteByPk(v.getId());
    }

    @RequestMapping(value = "deleteByIds",method = RequestMethod.DELETE)
    public void delete(@RequestBody List<String> ids){
        service.deleteByPks(ids);
    }

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public  List<LogDTO> list(@RequestBody LogDTO v){
        List<LogDTO> entitys = service.listDto(v);
        return entitys;
    }

    // @Logs(module = LogModule.LOG,operation = "查看日志")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public PageDto<LogDTO> listPage(@RequestBody PageDto<LogDTO> dto){
        return service.listPage(dto);
    }


}
