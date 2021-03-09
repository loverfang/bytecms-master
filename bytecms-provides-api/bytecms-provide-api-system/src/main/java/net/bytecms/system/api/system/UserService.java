package net.bytecms.system.api.system;

import net.bytecms.core.api.BaseService;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.system.dto.system.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService extends BaseService<UserDto> {
    /*
    根据用户名称查询用户
     */
	UserDto findUserByUsername(String username);

	boolean save(UserDto userDto);

    boolean deleteByUserId(String userId);

	boolean update(UserDto userDto);

    boolean deleteByIds(List<String> ids);

    boolean lockUsers(boolean justLock);
    /*
    获取用户个人信息以及角色权限
     */
    UserDto info(String userId);

    UserDto getById(String id);

    boolean batch(Integer type, List<String> ids);

    boolean updateUserInfo(UserDto userDto);

    List<UserDto> getUserByOrgId(String oid);

    void modifyPass(UserDto user);

    /**
     * 重置密码
     * @param id
     * @return
     */
    ApiResult resetPass(String id);

    /**
     * 查询用户角色标识
     * @param id
     * @return
     */
    Set<String> selectRoleSignByUserId(String id);

//
//	UserDto selectDetailById(Long id);
//
//	UserDto selectByEmail(String email);
//
//	/**
//	* @Description: 用户自己修改密码
//	* @param @param passWordDto
//	* @param @return
//	* @return boolean
//	* @throws
//	*/
//	//R updateMyPass(PassWordDto passWordDto);
//
//	/**查询角色名称
//	 * @param userId
//	 * @return
//	 */
//	String selectRoleName(Long userId);
//
//	List<String> selectRoleSign(Long userId);
//
//
//	List<UserDto> selectUserByDeptId(Long deptId);
//
//	/**修改用户的密码
//	 * @param userId
//	 * @return
//	 */
//	R updateUserPass(Long userId);
//
//	/**忘记密码构造链接
//	 */
//	//void buildEmailLink(UserDto user, HttpServletRequest request);
//
//	Map<String, Object> checkEmailLink(Map<String, String> map);
//

}
