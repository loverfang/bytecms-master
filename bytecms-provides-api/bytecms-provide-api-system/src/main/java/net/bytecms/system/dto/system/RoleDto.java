package net.bytecms.system.dto.system;

import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

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
public class RoleDto extends BaseModel {

    private Long roleId;

    /**
     * 角色名称
     */
    @Length(min = 1,max = 15,message = "角色长度在1-15之间")
    private String roleName;

    /**
     * 角色标识
     */
    @NotNull(message = "角色标识不能为空")
    private String roleSign;

    /**
     * 备注
     */
    private String remark;
}
