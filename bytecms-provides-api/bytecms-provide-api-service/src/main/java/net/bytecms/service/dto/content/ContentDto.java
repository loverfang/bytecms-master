package net.bytecms.service.dto.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.bytecms.core.annotation.DirectMark;
import net.bytecms.core.annotation.SolrMark;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.core.model.BaseCreateExtendModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 内容
 * </p>
 *
 * @author LG
 * @since 2019-10-30
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentDto extends BaseCreateExtendModel {

    private static final long serialVersionUID = 1L;


    /**
     * 标题
     */
    @DirectMark(desc = "标题",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    @SolrMark
    @NotBlank(message = "标题不能为空")
    private String title;


    /**
     * 发表用户
     */
    private String userId;


    /**
     * 审核用户
     */
    private String checkUserId;


    /**
     * 审核用户
     */
    private String checkUserName;



    /**
     * 分类
     */
    @SolrMark
    @DirectMark(desc = "分类ID",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    @NotBlank(message = "请选择分类")
    private String categoryId;

    /**
     * 分类名称
     */
    @DirectMark(desc = "分类名称",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private String categoryName;


    @DirectMark(desc = "分类CODE",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private String categoryCode;


    @DirectMark(desc = "分类URL",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private String categoryUrl;


    /**
     * 模型名称
     */
    private String modelName;


    /**
     * 模型
     */
    private String modelId;


    /**
     * 父内容ID
     */
    private Long parentId;


    /**
     * 引用内容ID
     */
    private Long quoteContentId;


    /**
     * 是否转载
     */
    @DirectMark(desc = "是否转载",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private Boolean copied=false;


    /**
     * 作者
     */
    @SolrMark
    @DirectMark(desc = "作者",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private String author;


    /**
     * 编辑
     */
    @SolrMark
    @DirectMark(desc = "编辑",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private String editor;


    /**
     * 外链
     */
    private Boolean onlyUrl;


    /**
     * 拥有图片列表
     */
    private Boolean hasImages;


    /**
     * 拥有附件列表
     */
    @DirectMark(desc = "是否含附件",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private Boolean hasFiles;


    /**
     * 已经静态化
     */
    @DirectMark(desc = "是否静态化",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private Boolean hasStatic;


    /**
     * 是否有推荐内容
     */
    @DirectMark(desc = "是否有推荐",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private Boolean hasRelated;


    /**
     * 设为推荐
     */
    private Boolean recommend;

    /**
     * 设为公告
     */
    private Boolean notice;



    /**
     * 设为热门
     */
    private Boolean hot;


    /**
     * 地址
     */
    @SolrMark
    @DirectMark(desc = "文章地址",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private String url;


    /**
     * 简介
     */
    @SolrMark
    @DirectMark(desc = "简介",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private String description;


    /**
     * 标签id
     */
    private String tagIds;


    /**
     * 标签
     */
    private List<String> tags = new ArrayList<>(16);


    /**
     * 标签
     */
    @SolrMark(name = "tags")
    private String tagString;


    /**
     * 数据字典值
     */
    private String dictionarValues;


    /**
     * 封面图像id
     */
    @DirectMark(desc = "封面地址",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private String cover;

    /**
     * 封面 链接地址
     */
    @SolrMark(name = "cover")
    private String coverPicUrl;


    /**
     * 子内容数
     */
    private Integer childs;


    /**
     * 分数
     */
    private Integer scores;


    /**
     * 评论数
     */
    private Integer comments;


    /**
     * 点击数
     */
    @DirectMark(desc = "点击次数",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private Integer clicks;

    /**
     * 点赞数
     */
    @DirectMark(desc = "点赞次数",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private Integer giveLikes;


    /**
     * 发布日期
     */
    @DirectMark(desc = "发布日期",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private Date publishDate;


    /**
     * 定时发布日期
     */
    private Date expiryDate;


    /**
     * 日期规则
     */
    private String rulesData;


    /**
     * 审核日期
     */
    private Date checkDate;


    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 置顶标签
     */
    private List<String> topTags;

    private String topTag;

    /**
     * 状态：0、草稿 1、已发布 2、待审核
     */
    @SolrMark
    private String status;

    private List<String> statuses;

    private List<String> categoryIds;

    private List<String> ids;

    private List<ContentFileDto> attachFiles;

    private List<CmsContentRelatedDto> cmsContentRelateds;

    @SolrMark(expand = true)
    @DirectMark(desc = "扩展字段",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private Map<String, Object> extendParam;

    @SolrMark
    @DirectMark(desc = "文章内容",groups = {DirectiveNameEnum.CMS_CONTENT_DIRECTIVE})
    private String text;

    private String data;

    private String extendFieldList;
    /**
     * 选中字段
     */
    private String checkedFieldList;

    @DirectMark
    private String templatePath;

    @DirectMark
    private String keywords;

}
