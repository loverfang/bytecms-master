package net.bytecms.service.entity.tags;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * <p>
 * 标签类型
 * </p>
 *
 * @author LG
 * @since 2020-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_tag_type")
public class CmsTagsType extends BaseModel {

private static final long serialVersionUID = 1L;
    /**
     * 名称
     */

        @TableField("name")
        private String name;

    /**
     * 标签数
     */

        @TableField("count")
        private Integer count;


}
