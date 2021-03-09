package net.bytecms.system.dto.system;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户与角色对应关系
 * </p>
 *
 * @author dl
 * @since 2018-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserRoleDto extends BaseModel {

    private static final long serialVersionUID = 1L;


    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private String roleId;



}
