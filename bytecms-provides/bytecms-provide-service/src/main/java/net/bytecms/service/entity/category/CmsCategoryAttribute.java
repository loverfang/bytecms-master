package net.bytecms.service.entity.category;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 分类扩展
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_category_attribute")
public class CmsCategoryAttribute extends BaseModel {

private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */

        @TableField("category_id")
        private String categoryId;


    /**
     * 标题(分类标题用于 SEO 关键字优化)
     */

        @TableField("title")
        private String title;


    /**
     * 关键词用于 SEO 关键字优化
     */

        @TableField("keywords")
        private String keywords;


    /**
     * 描述用于 SEO 关键字优化
     */

        @TableField("description")
        private String description;


    /**
     * 数据JSON
     */

        @TableField("data")
        private String data;


    /**
     * 创建用户id
     */






    /**
     * 创建时间
     */



    /**
     * 修改时间
     */






}
