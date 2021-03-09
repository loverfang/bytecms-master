package net.bytecms.web.controller.category;

import net.bytecms.core.annotation.Logs;
import net.bytecms.core.enumerate.LogModule;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.utils.Tree;
import net.bytecms.freemark.enums.ContentState;
import net.bytecms.service.api.category.CmsCategoryService;
import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 分类 前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
@Validated
@RestController
@RequestMapping("cmsCategory")
public class CmsCategoryController extends BaseController<CmsCategoryService> {


    @Logs(module = LogModule.CATEGORY,operation = "查看分类详情")
    @GetMapping("getInfoById")
    public CmsCategoryDto get(@NotBlank @RequestParam String id){
       return  service.getInfoById(id);
    }


    @Logs(module = LogModule.CATEGORY,operation = "创建栏目分类")
    @PostMapping(value="save")
    public void save(@Validated @RequestBody CmsCategoryDto v){
        service.save(v);
    }

    @Logs(module = LogModule.CATEGORY,operation = "更新栏目分类")
    @PutMapping("update")
    public void update(@RequestBody CmsCategoryDto v){
        service.update(v);
    }


    @Logs(module = LogModule.CATEGORY,operation = "删除栏目分类")
    @DeleteMapping("deleteById")
    public boolean deleteByPk(@NotBlank @RequestParam String pk) {
         return service.deleteByPk(pk);
    }


    @Logs(module = LogModule.CATEGORY,operation = "删除栏目分类")
    @DeleteMapping("deleteByCategoryId")
    public boolean deleteByCategoryId(@NotBlank @RequestParam String id) {
        return service.deleteByCategoryId(id);
    }

    @DeleteMapping(value = "deleteByIds")
    public void deleteByPks(@NotEmpty @RequestBody List<String> ids){
          service.deleteByPks(ids);
    }

    @PostMapping("list")
    public  List<CmsCategoryDto> list(@RequestBody CmsCategoryDto v){
        return service.listDto(v);
    }

    @PostMapping("page")
    public PageDto<CmsCategoryDto> listPage(@RequestBody PageDto<CmsCategoryDto> pageDto){
        pageDto.getDto().condition().orderByAsc("sort").eq("parent_id", pageDto.getDto().getId());
        return service.listPage(pageDto);
    }


    // 内容发布查询栏目
    @RequestMapping("/selectTreeCategoryAuth")
    public Tree<CmsCategoryDto> selectTreeCategoryAuth() {
        Tree<CmsCategoryDto> treeCategorys = service.selectTreeCategory(true);
        return treeCategorys;
    }


    // 栏目管理用
    @RequestMapping("/selectTreeCategory")
    public Tree<CmsCategoryDto> selectTreeCategory() {
        Tree<CmsCategoryDto> treeCategorys = service.selectTreeCategory(false);
        return treeCategorys;
    }


    @Logs(module = LogModule.CATEGORY,operation = "重新静态化栏目")
    @GetMapping(value="reStaticFileByCid")
    public void reStaticFileByCid(@NotBlank @RequestParam String id){
        String[]  status = {ContentState.PUBLISH.getCode()};
        service.reStaticFileByCid(id,Arrays.asList(status));
    }


    @Logs(module = LogModule.CATEGORY,operation = "更新内容模型")
    @PutMapping(value="updateContentModel")
    public void updateContentModel(@NotBlank @RequestParam String id){
        service.updateContentModel(id);
    }


}
