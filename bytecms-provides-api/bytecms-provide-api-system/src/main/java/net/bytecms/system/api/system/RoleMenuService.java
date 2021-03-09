package net.bytecms.system.api.system;

import net.bytecms.core.api.BaseService;
import net.bytecms.system.dto.system.MenuDto;
import net.bytecms.system.dto.system.RoleMenuDto;
import net.bytecms.core.utils.Tree;

import java.util.List;

/**
 * <p>
 * 角色与菜单对应关系 服务类
 * </p>
 *
 * @author dl
 * @since 2018-03-23
 */
public interface RoleMenuService extends BaseService<RoleMenuDto> {

	Tree<MenuDto> selectTreeMenuByUser(String roleId, String userId);

	boolean assignMenu(RoleMenuDto roleMenuDto);
	
	boolean deleteByRoleId(String roleId);
	
	boolean deleteByMenuId(String menuId);

    List<RoleMenuDto> getsByMenuId(String parentId);
}
