package net.bytecms.system.entity.system;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author dl
 * @since 2018-03-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_role")
public class Role extends BaseModel<Role> {


    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 角色标识
     */
    @TableField("role_sign")
   // @NotNull(message = "角色标识不能为空", groups = {Insert.class})
    private String roleSign;

    /**
     * 备注
     */
    private String remark;

}
