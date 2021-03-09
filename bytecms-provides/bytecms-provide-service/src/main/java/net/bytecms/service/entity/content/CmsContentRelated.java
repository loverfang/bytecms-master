package net.bytecms.service.entity.content;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 内容推荐
 * </p>
 *
 * @author LG
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_content_related")
public class CmsContentRelated extends BaseModel {

private static final long serialVersionUID = 1L;


        @TableId("id")
        private String id;


    /**
     * 当前文章内容id
     */

        @TableField("content_id")
        private String contentId;


    /**
     * 推荐内容id
     */

        @TableField("related_content_id")
        private String relatedContentId;


    /**
     * 推荐用户
     */

        @TableField("user_id")
        private String userId;


    /**
     * 推荐链接地址
     */

        @TableField("url")
        private String url;


    /**
     * 推荐标题
     */

        @TableField("title")
        private String title;


    /**
     * 推荐简介
     */

        @TableField("description")
        private String description;


    /**
     * 点击数
     */

        @TableField("clicks")
        private Integer clicks;


    /**
     * 排序
     */

        @TableField("sort")
        private Integer sort;


    /**
     * 创建用户id
     */



    /**
     * 修改人id
     */



    /**
     * 创建时间
     */



    /**
     * 修改时间
     */






}
