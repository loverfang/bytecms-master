package net.bytecms.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BaseCreateExtendModel extends BaseModel {

    /**
     * 创建人名称
     */
    @TableField(value = "create_name")
    private String createName;

    /**
     * 修改人名称
     */
    @TableField(value = "modified_name")
    private String modifiedName;

}
