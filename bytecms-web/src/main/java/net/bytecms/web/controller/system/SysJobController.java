package net.bytecms.web.controller.system;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import net.bytecms.core.annotation.Logs;
import net.bytecms.core.enumerate.LogModule;
import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.bytecms.core.model.PageDto;
import net.bytecms.system.api.system.SysJobService;
import net.bytecms.system.dto.system.SysJobDto;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-11-19
 */
@Validated
@RestController
@RequestMapping("sysJob")
public class SysJobController extends BaseController<SysJobService> {


    @Logs(module = LogModule.TASK,operation = "获取任务详情")
    @GetMapping("getByPk")
    public SysJobDto get(@NotBlank @RequestParam String id){
       return  service.getByPk(id);
    }

    @PostMapping(value="save")
    public void save(@Validated @RequestBody SysJobDto v){
        service.insert(v);
    }

    @PutMapping("update")
    public void update(@RequestBody SysJobDto v){
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
    public  List<SysJobDto> list(@RequestBody SysJobDto v){
        return service.listDto(v);
    }

    @Logs(module = LogModule.TASK,operation = "查看任务列表")
    @PostMapping("page")
    public PageDto<SysJobDto> listPage(@RequestBody PageDto<SysJobDto> pageDto){
        return service.listPage(pageDto);
    }

    /**
     * 任务启动、暂停、删除
     * @param v
     */
    @Logs(module = LogModule.TASK,operation = "任务启动、暂停、删除任务")
    @PutMapping("taskAction")
    public void taskAction(@Validated @RequestBody SysJobDto v){
        service.taskAction(v);
    }

}
