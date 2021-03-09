package net.bytecms.web.controller.fragment;

import net.bytecms.service.api.fragment.FragmentAttributeService;
import net.bytecms.service.dto.fragment.FragmentAttributeDto;
import net.bytecms.core.model.PageDto;
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
 * @since 2019-11-07
 */
@Validated
@RestController
@RequestMapping("api/fragmentAttribute")
public class FragmentAttributeController extends BaseController<FragmentAttributeService> {


    @GetMapping("getByPk")
    public FragmentAttributeDto get(@NotBlank @RequestParam String id){
       return  service.getByPk(id);
    }

    @PostMapping(value="save")
    public void save(@Validated @RequestBody FragmentAttributeDto v){
        service.insert(v);
    }

    @PutMapping("update")
    public void update(@RequestBody FragmentAttributeDto v){
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
    public  List<FragmentAttributeDto> list(@RequestBody FragmentAttributeDto v){
        return service.listDto(v);
    }

    @PostMapping("page")
    public PageDto<FragmentAttributeDto> listPage(@RequestBody PageDto<FragmentAttributeDto> pageDto){
        return service.listPage(pageDto);
    }

}
