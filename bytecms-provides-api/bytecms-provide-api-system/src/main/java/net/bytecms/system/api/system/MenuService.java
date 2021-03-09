package net.bytecms.system.api.system;

import net.bytecms.core.api.BaseService;
import net.bytecms.system.dto.system.MenuDto;
import net.bytecms.core.utils.Tree;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author dl
 * @since 2018-03-21
 */
public interface MenuService extends BaseService<MenuDto> {


	boolean save(MenuDto menu);

	boolean update(MenuDto menu);

	Tree<MenuDto> selectTreeList();

	List<MenuDto> selectMenuTreeByUid(String userId);
	
	Set<String> selectPermsByUid(String userId);

	void clearPermsCacheByUid(String userId);

	Set<String> selectPermsByUrl(String url);
	/** 
	* @Description: 根据登录人查询其所拥有的菜单
	* @param @param userId
	* @param @return  
	* @return List<Menu> 
	* @throws 
	*/ 
	List<MenuDto> selectMenuUid(String userId);

    MenuDto info(String id);

	boolean delete(String menuId);
}
