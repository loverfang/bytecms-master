package net.bytecms.web.controller.content;
import java.util.List;

import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import net.bytecms.service.api.content.ContentAttributeService;
import net.bytecms.service.dto.content.ContentAttributeDto;
import net.bytecms.core.model.PageDto;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 内容扩展 前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-10-30
 */
@Validated
@RestController
@RequestMapping("api/contentAttribute")
public class ContentAttributeController extends BaseController<ContentAttributeService> {


    @GetMapping("getByPk")
    public ContentAttributeDto get(@NotBlank @RequestParam String id){
       return  service.getByPk(id);
    }

    @PostMapping(value="save")
    public void save(@Validated @RequestBody ContentAttributeDto v){
        service.insert(v);
    }

    @PutMapping("update")
    public void update(@RequestBody ContentAttributeDto v){
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
    public  List<ContentAttributeDto> list(@RequestBody ContentAttributeDto v){
        return service.listDto(v);
    }

    @PostMapping("page")
    public PageDto<ContentAttributeDto> listPage(@RequestBody PageDto<ContentAttributeDto> pageDto){
        return service.listPage(pageDto);
    }

}
