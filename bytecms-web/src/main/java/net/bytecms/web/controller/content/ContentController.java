package net.bytecms.web.controller.content;

import net.bytecms.core.annotation.Logs;
import net.bytecms.core.enumerate.LogModule;
import net.bytecms.core.enumerate.LogOperation;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.service.api.content.ContentService;
import net.bytecms.service.dto.content.ContentDto;
import net.bytecms.web.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 内容 前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-10-30
 */
@Validated
@RestController
@RequestMapping("content")
public class ContentController extends BaseController<ContentService> {

    @Logs(module = LogModule.CONTENT,operation = "查看内容详情")
    @GetMapping("getByPk")
    public ApiResult get(@NotBlank @RequestParam String id){
       return  service.getInfoByPk(id);
    }

    @Logs(module = LogModule.CONTENT,operation = "查询置顶标签")
    @GetMapping("getTopTag")
    public Set<String> getTopTag(){
        Set<String> topTags=  service.getTopTag();
        return topTags;
    }

    @Logs(module = LogModule.CONTENT,operation = "置顶内容")
    @PutMapping("top")
    public ApiResult top(@NotNull @RequestBody ContentDto contentDto){
        return  service.top(contentDto);
    }


    @Logs(module = LogModule.CONTENT,operation = "移动内容到指定栏目的")
    @PutMapping("move")
    public ApiResult move(@RequestBody ContentDto contentDto){
        return  service.move(contentDto.getCategoryId(),contentDto.getIds());
    }

    @Logs(module = LogModule.CONTENT,operation = "删除内容")
    @DeleteMapping("deleteByPk")
    public boolean deleteByPk(@NotBlank @RequestParam String pk) {
         return service.deleteContentByPk(pk);
    }

    @DeleteMapping(value = "deleteByIds")
    public void deleteByPks(@NotEmpty @RequestBody List<String> ids){
          service.deleteContentByPks(ids);
    }

    @PostMapping("list")
    public  List<ContentDto> list(@RequestBody ContentDto v){
        return service.listDto(v);
    }

    @PostMapping("page")
    public PageDto<ContentDto> listPage(@RequestBody PageDto<ContentDto> pageDto){
        return service.listPage(pageDto);
    }

    @PostMapping("pageRecycler")
    public PageDto<ContentDto> pageRecycler(@RequestBody PageDto<ContentDto> pageDto){
        return service.pageRecycler(pageDto);
    }

    @GetMapping("getCreateForm")
    public  ApiResult getCreateForm(@NotBlank @RequestParam String categoryId ){
        return service.getCreateForm(categoryId);
    }

    @Logs(module = LogModule.CONTENT,operaEnum= LogOperation.SAVE,operation = "新增内容")
    @PostMapping(value="saveContent")
    public void saveContent(@Validated @RequestBody ContentDto v){
        service.saveContent(v);
    }

    @Logs(module = LogModule.CONTENT,operaEnum= LogOperation.UPDATE,operation = "修改内容")
    @PutMapping("updateContent")
    public void updateContent(@RequestBody ContentDto v){
        service.updateContent(v);
    }


    @Logs(module = LogModule.CONTENT,operation = "发布/撤销内容")
    @PutMapping("publish")
    public void publish(@RequestBody ContentDto v){
        service.publish(v,0);
    }


    @Logs(module = LogModule.CONTENT,operation = "定时发布/撤销内容")
    @PutMapping("jobPublish")
    public void jobPublish(@RequestBody ContentDto v){
        service.jobPublish(v);
    }


    @Logs(module = LogModule.CONTENT,operation = "重新批量生成静态页")
    @PostMapping(value="reStaticBatchGenCid")
    public void reStaticFileByCid(@NotEmpty @RequestBody List<String> ids){
        service.reStaticBatchGenCid(ids,getUserId());
    }


    @Logs(module = LogModule.CONTENT,operation = "彻底删除")
    @DeleteMapping(value="delByPksfinalDoIt")
    public void finalDoIt(@RequestBody ContentDto contentDto){
        service.finalDoIt(contentDto.getIds(),contentDto.getStatus());
    }

}
