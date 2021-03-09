package net.bytecms.service.mapper.content;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bytecms.service.dto.content.ContentDto;
import net.bytecms.service.entity.content.Content;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 内容 Mapper 接口
 * </p>
 *
 * @author LG
 * @since 2019-10-30
 */
@Mapper
public interface ContentMapper extends BaseMapper<Content> {

    /**
     * 查询文章数据
     * @param pages
     * @return
     */
    IPage<ContentDto> listPage(IPage<ContentDto> pages, @Param("dto") ContentDto dto);

    /**
     * 用于生成分类列表静态页查询数据
     * @param pages
     * @param categoryId
     * @return
     */
    IPage<ContentDto> pageContentForCategoryGen(IPage<ContentDto> pages,  @Param("categoryId")  String categoryId, @Param("status")  List<String> status);

    List<ContentDto> selectContentByCategorys(@Param("codes") String[] codes, @Param("maxRowNum") Integer maxRowNum);

    /**
     * 单页查询
     * @param categoryId
     * @return
     */
    ContentDto selectSinglePageContent(@Param("categoryId") String categoryId);

    void updateContentModel(@Param("categoryId") String categoryId, @Param("modelId") String modelId,@Param("condition") String condition );

    /**
     * 批量生成静态页
     * @param ids
     * @return
     */
    List<ContentDto> reStaticBatchGenCid(@Param("ids") List<String> ids);

    IPage<ContentDto> pageAllContentForToSolr(IPage<ContentDto> pages, List<String> status);

    /**
     * 查询所有文章用于全站生成
     * @param pages
     * @param status
     * @return
     */
    IPage<ContentDto> pageAllContentForGen(IPage<ContentDto> pages, List<String> status);

    IPage<ContentDto> searchByTag(IPage<ContentDto> pages,@Param("tagId") String tagId);

    /**
     * 根据内容id 查询详情用于指令
     * @param contentId
     * @return
     */
    ContentDto getContentById(@Param("contentId") String contentId);

    /**
     * 重写获取文章详情的方法
     * @param pk
     * @return
     */
    ContentDto getByPk(Serializable pk);

    /**
     * 获取上一篇或者下一篇
     * @param contentId
     * @param categoryId
     * @param isNext
     * @return
     */
    ContentDto getNextOrPreviousContentByIdAndCateg(@Param("contentId") String contentId, @Param("categoryId") String categoryId, @Param("isNext") boolean isNext);

    List<String> getTopTag();

    List<ContentDto> getContentsByTopTag(@Param("tag") String tag, @Param("rowNum") Integer rowNum,@Param("categoryId") String categoryId);

    List<ContentDto> selectContentBySingleCategory(@Param("code") String code, @Param("rowNum") Integer maxRowNum);
}
