package net.bytecms.service.entity.category;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * <p>
 * 分类-模型 关系表 一个分类可以针对不同的模型投稿（本系统暂时只是一对一后期可扩展）
 * </p>
 *
 * @author LG
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_category_model")
public class CmsCategoryModel extends BaseModel {

private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */

        @TableField("category_id")
        private String categoryId;


    /**
     * 模型编码
     */

        @TableField("model_id")
        private String modelId;


    /**
     * 内容模板路径
     */

        @TableField("template_path")
        private String templatePath;








}
