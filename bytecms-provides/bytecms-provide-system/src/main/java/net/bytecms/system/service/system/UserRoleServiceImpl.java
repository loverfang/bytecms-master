package net.bytecms.system.service.system;

import net.bytecms.system.api.system.UserRoleService;
import net.bytecms.system.dto.system.UserRoleDto;
import net.bytecms.system.mapper.system.UserRoleMapper;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.system.entity.system.UserRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户与角色对应关系 服务实现类
 * </p>
 *
 * @author dl
 * @since 2018-03-23
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleDto, UserRole, UserRoleMapper> implements UserRoleService {

    @Override
    public List<String> selectRoleIdByUId(String userId) {
        List<String> roleIds = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("user_id", userId);
        List<UserRole> userRoles = (List<UserRole>) listByMap(param);
        for (UserRole userRole : userRoles) {
            roleIds.add(userRole.getRoleId());
        }
        return roleIds;
    }

    @Override
    public List<String> selectUserIdByRId(String roleId) {
        List<String> userIds = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("role_id", roleId);
        List<UserRole> userRoles = (List<UserRole>) listByMap(param);
        for (UserRole UserRole : userRoles) {
            userIds.add(UserRole.getUserId());
        }
        return userIds;
    }

    @Override
    public boolean deleteByRoleId(String roleId) {
        Map<String, Object> param = new HashMap<>();
        param.put("role_id", roleId);
        return removeByMap(param);
    }

    @Override
    public boolean deleteByUserId(String userId) {
        Map<String, Object> param = new HashMap<>();
        param.put("user_id", userId);
        return removeByMap(param);
    }

    @Override
    public boolean insertUserRoleBatch(List<UserRoleDto> list) {
        return insertBatch(list);
    }


}
