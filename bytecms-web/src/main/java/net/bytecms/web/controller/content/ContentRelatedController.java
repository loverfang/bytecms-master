package net.bytecms.web.controller.content;
import java.util.List;

import com.google.common.collect.Lists;
import net.bytecms.service.api.content.CmsContentRelatedService;
import net.bytecms.service.dto.content.CmsContentRelatedDto;
import net.bytecms.core.utils.Checker;
import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import net.bytecms.core.model.PageDto;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 内容推荐 前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-12-12
 */
@Validated
@RestController
@RequestMapping("contentRelated")
public class ContentRelatedController extends BaseController<CmsContentRelatedService> {


    @GetMapping("getByPk")
    public CmsContentRelatedDto get(@NotBlank @RequestParam String id){
       return  service.getByPk(id);
    }

    @PostMapping(value="save")
    public void save(@Validated @RequestBody CmsContentRelatedDto v){
        service.insert(v);
    }

    @PutMapping("update")
    public void update(@RequestBody CmsContentRelatedDto v){
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
    public  List<CmsContentRelatedDto> list(@RequestBody CmsContentRelatedDto v){
        return service.listDto(v);
    }

    @PostMapping("page")
    public PageDto<CmsContentRelatedDto> listPage(@RequestBody PageDto<CmsContentRelatedDto> pageDto){
        if(Checker.BeBlank(pageDto.getDto().getContentId())){
            return  new PageDto<>(0,pageDto.getPageSize(),pageDto.getPageNo(), Lists.newArrayList());
        }
        pageDto.setPageSize(50);
        return service.listPage(pageDto);
    }

}
