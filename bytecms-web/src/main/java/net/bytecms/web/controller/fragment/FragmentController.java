package net.bytecms.web.controller.fragment;
import java.util.List;

import net.bytecms.core.annotation.Logs;
import net.bytecms.core.enumerate.LogModule;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import net.bytecms.service.api.fragment.FragmentService;
import net.bytecms.service.dto.fragment.FragmentDto;
import net.bytecms.core.model.PageDto;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 页面片段数据表 前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
@Validated
@RestController
@RequestMapping("fragment")
public class FragmentController extends BaseController<FragmentService> {


    @Logs(module = LogModule.FRAGMENT,operation = "获取页面片段数据详情")
    @GetMapping("getInfoByPk")
    public ApiResult get(@NotBlank @RequestParam String id){
       return  service.getInfoByPk(id);
    }

    @Logs(module = LogModule.FRAGMENT,operation = "保存页面片段数据")
    @PostMapping(value="saveFragment")
    public void saveFragment(@Validated @RequestBody FragmentDto v){
        service.saveFragment(v);
    }


    @Logs(module = LogModule.FRAGMENT,operation = "更新页面片段数据")
    @PutMapping("updateFragment")
    public void updateFragment(@RequestBody FragmentDto v){
        service.updateFragmentByPk(v);
    }

    @Logs(module = LogModule.FRAGMENT,operation = "删除页面片段数据")
    @DeleteMapping("deleteFragmentByPk")
    public boolean deleteFragmentByPk(@NotBlank @RequestParam String id) {
         return service.deleteFragmentByPk(id);
    }

    @DeleteMapping(value = "deleteByIds")
    public void deleteByPks(@NotEmpty @RequestBody List<String> ids){
          service.deleteByPks(ids);
    }

    @PostMapping("list")
    public  List<FragmentDto> list(@RequestBody FragmentDto v){
        return service.listDto(v);
    }

    @Logs(module = LogModule.FRAGMENT,operation = "查看页面片段数据列表")
    @PostMapping("page")
    public PageDto<FragmentDto> listPage(@RequestBody PageDto<FragmentDto> pageDto){
        pageDto.getDto().condition().eq("fragment_file_model_id",pageDto.getDto().getFragmentFileModelId());
        return service.listPage(pageDto);
    }

}
