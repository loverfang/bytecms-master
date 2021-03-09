package net.bytecms.system.dto.system;

import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 角色与菜单对应关系
 * </p>
 *
 * @author dl
 * @since 2018-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RoleMenuDto extends BaseModel<RoleMenuDto> {


    /**
     * 角色ID
     */
    private String roleId;

    private Integer halfChecked;

    /**
     * 菜单ID
     */
    private String menuId;

    private List<String> onCheckKeys;//全选中

    private List<String> halfCheckedKeys;//半选中


}
