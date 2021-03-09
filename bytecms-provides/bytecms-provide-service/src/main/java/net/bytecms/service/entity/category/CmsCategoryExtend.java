package net.bytecms.service.entity.category;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("thinkcms_category_extend")
public class CmsCategoryExtend extends BaseModel {

private static final long serialVersionUID = 1L;


        @TableId("id")
        private String id;


    /**
     * 分类扩展名称
     */

        @TableField("category_extend_name")
        private String categoryExtendName;


    /**
     * 扩展字段(json 保存)
     */

        @TableField("extend_field_list")
        private String extendFieldList;


    /**
     * 排序
     */

        @TableField("sort")
        private Integer sort;


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
