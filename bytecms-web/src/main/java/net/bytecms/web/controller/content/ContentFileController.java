package net.bytecms.web.controller.content;
import java.util.List;
import net.bytecms.service.api.content.ContentFileService;
import net.bytecms.service.dto.content.ContentFileDto;
import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import net.bytecms.core.model.PageDto;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 内容附件 前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-12-07
 */
@Validated
@RestController
@RequestMapping("api/contentFile")
public class ContentFileController extends BaseController<ContentFileService> {


    @GetMapping("getByPk")
    public ContentFileDto get(@NotBlank @RequestParam String id){
       return  service.getByPk(id);
    }

    @PostMapping(value="save")
    public void save(@Validated @RequestBody ContentFileDto v){
        service.insert(v);
    }

    @PutMapping("update")
    public void update(@RequestBody ContentFileDto v){
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
    public  List<ContentFileDto> list(@RequestBody ContentFileDto v){
        return service.listDto(v);
    }

    @PostMapping("page")
    public PageDto<ContentFileDto> listPage(@RequestBody PageDto<ContentFileDto> pageDto){
        return service.listPage(pageDto);
    }

}
