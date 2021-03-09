package net.bytecms.service.entity.model;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 
 * </p>
 *
 * @author LG
 * @since 2019-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_model")
public class CmsModel extends BaseModel {

private static final long serialVersionUID = 1L;



        @TableField("parent_id")
        private String parentId;


    /**
     * 模型名称
     */

        @TableField("name")
        private String name;


    /**
     * 静态化模板地址
     */

        @TableField("template_path")
        private String templatePath;



        @TableField("has_child")
        private Boolean hasChild;


    /**
     * 是否有用图片
     */

        @TableField("has_images")
        private Boolean hasImages;


    /**
     * 是否拥有文件
     */

        @TableField("has_files")
        private Boolean hasFiles;



        @TableField("is_url")
        private Integer isUrl;


    /**
     * 默认字段
     */

        @TableField("default_field_list")
        private String defaultFieldList;


    /**
     * 选中字段
     */

    @TableField("checked_field_list")
    private String checkedFieldList;




    /**
     * 扩展字段
     */

        @TableField("extend_field_list")
        private String extendFieldList;


    /**
     * 必填字段
     */

        @TableField("required_field_list")
        private String requiredFieldList;


    /**
     * 字段对应中文名称
     */

        @TableField("field_text_map")
        private String fieldTextMap;


}
