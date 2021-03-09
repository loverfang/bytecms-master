package net.bytecms.service.entity.content;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.annotation.FieldDefault;
import net.bytecms.core.constants.InputTypeEnum;
import net.bytecms.core.model.BaseCreateExtendModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 内容
 * </p>
 *
 * @author LG
 * @since 2019-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_content")
public class Content extends BaseCreateExtendModel {

    private static final long serialVersionUID = 1L;


    /**
     * 标题
     */

    @FieldDefault(fieldNote = "标题", visibleSwitch = false, visibleCheck = true, initCheck = true, initSwitch = true)
    @TableField("title")
    private String title;


    /**
     * 发表用户
     */

    @TableField("user_id")
    private String userId;


    /**
     * 审核用户
     */

    @TableField("check_user_id")
    private String checkUserId;


    @TableField("check_user_name")
    private String checkUserName;


    /**
     * 分类
     */

    @TableField("category_id")
    private String categoryId;


    /**
     * 模型
     */

    @TableField(value = "model_id",updateStrategy= FieldStrategy.IGNORED)
    private String modelId;


    /**
     * 父内容ID
     */

    @TableField("parent_id")
    private String parentId;


    /**
     * 引用内容ID
     */

    @TableField("quote_content_id")
    private String quoteContentId;


    /**
     * 是否转载
     */

    @FieldDefault(fieldNote = "转载", visibleSwitch = false,inputType = InputTypeEnum.BOOLEAN)
    @TableField("copied")
    private Boolean copied;
    /**
     * 标签
     */
    @FieldDefault(fieldNote = "标签", visibleSwitch = false,inputType = InputTypeEnum.TAGS)
    @TableField(exist = false)
    private List<String> tags;


    /**
     * 标签
     */
    @TableField("tag_ids")
    private String tagIds;


    /**
     * 作者
     */

    @FieldDefault(fieldNote = "作者")
    @TableField("author")
    private String author;


    /**
     * 编辑
     */
    @FieldDefault(fieldNote = "编辑")
    @TableField("editor")
    private String editor;


    /**
     * 外链
     */

    @TableField("only_url")
    private Boolean onlyUrl;


    /**
     * 拥有图片列表
     */

    @TableField("has_images")
    private Boolean hasImages;


    /**
     * 拥有附件列表
     */

    @TableField("has_files")
    private Boolean hasFiles;


    /**
     * 已经静态化
     */

    @TableField("has_static")
    private Boolean hasStatic;


    /**
     * 是否有推荐内容
     */
    @TableField("has_related")
    private Boolean hasRelated;




    /**
     * 设为推荐
     */
    @TableField("recommend")
    private Boolean recommend;

    /**
     * 设为公告
     */
    @TableField("notice")
    private Boolean notice;


    /**
     * 设为热门
     */
    @TableField("hot")
    private Boolean hot;


    /**
     * 地址
     */

    @TableField("url")
    private String url;


    /**
     * 简介
     */
    @FieldDefault(fieldNote = "简介",inputType = InputTypeEnum.TEXTAREA)
    @TableField("description")
    private String description;


    /**
     * 数据字典值
     */

    @TableField("dictionar_values")
    private String dictionarValues;


    /**
     * 封面
     */
    @FieldDefault(fieldNote = "封面", inputType = InputTypeEnum.PICTURE)
    @TableField("cover")
    private String cover;


    /**
     * 子内容数
     */

    @TableField("childs")
    private Integer childs;


    /**
     * 分数
     */

    @TableField("scores")
    private Integer scores;


    /**
     * 评论数
     */

    @TableField("comments")
    private Integer comments;


    /**
     * 点击数
     */

    @TableField("clicks")
    private Integer clicks;



    /**
     * 点赞数
     */
    @TableField("give_likes")
    private Integer giveLikes;



    /**
     * 发布日期
     */

    @TableField("publish_date")
    private Date publishDate;


    /**
     * 定时日期
     */

    @TableField(value = "expiry_date",updateStrategy = FieldStrategy.IGNORED)
    private Date expiryDate;


    /**
     * 审核日期
     */

    @TableField("check_date")
    private Date checkDate;


    /**
     * 日期规则
     */

    @TableField("rules_data")
    private String rulesData;



    /**
     * 顺序
     */

    @TableField("sort")
    private Integer sort;


    /**
     * 置顶标签
     */
    @TableField("top_tag")
    private String topTag;

    /**
     * 状态：0、草稿 1、已发布 2、待审核
     */

    @TableField("status")
    private String status;


}
