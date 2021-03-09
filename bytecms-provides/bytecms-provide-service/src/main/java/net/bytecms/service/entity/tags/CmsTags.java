package net.bytecms.service.entity.tags;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * <p>
 * 标签
 * </p>
 *
 * @author LG
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_tag")
public class CmsTags extends BaseModel {

private static final long serialVersionUID = 1L;
    /**
     * 名称
     */
        @TableField("name")
        private String name;

    /**
     * 分类ID
     */
        @TableField("type_id")
        private String typeId;


}
