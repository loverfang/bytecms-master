package net.bytecms.service.api.content;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bytecms.core.api.BaseService;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.model.SolrSearchModel;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.service.dto.content.ContentDto;
import net.bytecms.service.dto.tags.CmsTagsDto;
import org.apache.solr.common.SolrDocument;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 内容 服务类
 * </p>
 *
 * @author LG
 * @since 2019-10-30
 */
public interface ContentService extends BaseService<ContentDto> {


    /**
     * 添加文章时 根据栏目模型构建表单
     * @param categoryId
     * @return
     */
    ApiResult getCreateForm(String categoryId);

    /**
     * 保存创建文章
     * @param v
     */
    void saveContent(ContentDto v);

    /**
     * 更新内容
     * @param v
     */
    void updateContent(ContentDto v);

    /**
     * 编辑内容获取详情
     * @param id
     * @return
     */
    ApiResult getInfoByPk(String id);


    /**
     * 删除文章
     * @param pk
     * @return
     */
    boolean deleteContentByPk(String pk);

    /**
     * 批量删除文章
     * @param ids
     */
    void deleteContentByPks(List<String> ids);

    /**
     * 侧地删除
     * @param ids
     */
    void realDelContentByPks(List<String> ids);
    /**
     * 批量发布或者取消发布
     * @param v
     * @param pubWay 0:手动 1：定时
     */
    void publish(ContentDto v,int pubWay);


    /**
     * 用于生成分类列表静态页查询数据
     * @param pageDto
     * @return
     */
    List<ContentDto> pageContentForCategoryGen(PageDto<ContentDto> pageDto);

    /**
     * 根据分类计算分类总页数
     * @param categoryId
     * @return
     */
    Integer calculatePageCountByCategoryId(String categoryId,Integer pageSize);

    /**
     * 根据栏目 code 查询栏目下的内容
     * @param codes
     * @param maxRowNum
     * @return
     */
    Map<String, Object> selectContentByCategory(String[] codes, Integer maxRowNum);

    List<ContentDto> selectContentBySingleCategory(String code, Integer maxRowNum);

    boolean ckHasContentByCategId(String categoryId,List<String> status);


    /**
     * 根据分类id查询当前分类下的第一条记录 用于单页查询展示
     * @param categoryId
     * @return
     */
    ContentDto selectSinglePageContent(String categoryId);


    /**
     * 根据文章获取文章内容
     * @param contentId
     * @return
     */
    ContentDto getContentById(String contentId);


    /**
     * 根据置顶标识获取内容
     * @param tag
     * @return
     */
    List<ContentDto> getContentsByTopTag(String tag,int rowNum,String categoryId);

    /**
     * 根据关键字查询
     * @param pageDto
     * @return
     */
    PageDto<SolrDocument> searchKeyWord(PageDto<SolrSearchModel> pageDto);

    /**
     * 置顶
     * @param contentDto
     * @return
     */
    ApiResult top(ContentDto contentDto);

    /**
     * 更新内容模型
     * @param categoryId
     * @param modelId
     */
    void updateContentModel(String categoryId, String modelId,String condition);

    /**
     * 移动文章到指定栏目
     * @param categoryId
     * @param ids
     * @return
     */
    ApiResult move(String categoryId, List<String> ids);

    /**
     *
     * @param contentId
     * @param categoryId
     * @param isNext true 下一个 false 上一个
     * @return
     */
    ContentDto getNextOrPreviousContentByIdAndCateg(String contentId, String categoryId, boolean isNext);

    /**
     * 批量生成静态页
     * @param id
     */
    void reStaticBatchGenCid(List<String> id,String userId);

    /**
     * 获取文章的点击次数
     * @param contentId
     * @return
     */
    Long searchClicks(String contentId);


    /**
     * 点击排行
     * @return
     */
    List<ContentDto> clicksTopTotal(Integer  maxRowNum,String categoryId);


    /**
     * 查询所有文章 同步solr 库
     * @param pages
     * @param asList
     * @return
     */
    IPage<ContentDto> pageAllContentForToSolr(IPage<ContentDto> pages, List<String> asList);

    /**
     * 获取推荐或者公告以及最新的文章内容
     * @param directive
     * @param categoryId
     * @return
     */
    List<ContentDto> getTopContent(DirectiveNameEnum directive, Integer  rowNum, String categoryId);

    /**
     * 获取点赞数
     * @param contentId
     * @return
     */
    ApiResult searchGiveLikes(String contentId,String userAgent,Boolean isClick);

    /**
     * 根据关键字查询文章
     * @param pageDto
     * @return
     */
    PageDto<ContentDto> searchByTag(PageDto<CmsTagsDto> pageDto);

    /**
     * 定时发布任务
     * @param v
     */
    void jobPublish(ContentDto v);

    /**
     * 查询回收站
     * @param pageDto
     * @return
     */
    PageDto<ContentDto> pageRecycler(PageDto<ContentDto> pageDto);


    /**
     * 彻底删除/恢复操作
     * @param ids
     */
    void finalDoIt(List<String> ids,String status);


    /**
     * 查询所有的内容用于生成静态页
     * @param pages
     * @param status
     * @return
     */
    IPage<ContentDto> pageAllContentForGen(IPage<ContentDto> pages, List<String> status);

    Set<String> getTopTag();
}