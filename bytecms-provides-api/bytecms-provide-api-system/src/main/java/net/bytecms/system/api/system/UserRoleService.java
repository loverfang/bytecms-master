package net.bytecms.system.api.system;

import net.bytecms.core.api.BaseService;
import net.bytecms.system.dto.system.UserRoleDto;

import java.util.List;

/**
 * <p>
 * 用户与角色对应关系 服务类
 * </p>
 *
 * @author dl
 * @since 2018-03-23
 */
public interface UserRoleService extends BaseService<UserRoleDto> {

	/**
	 * @param //根据用户的id查找其所有的角色Id
	 * @return
	 */
	List<String> selectRoleIdByUId(String userId);
	
	List<String> selectUserIdByRId(String roleId);
	
	boolean deleteByRoleId(String roleId);
	
	boolean deleteByUserId(String userId);

    boolean insertUserRoleBatch(List<UserRoleDto> list);
}
