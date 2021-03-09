package net.bytecms.system.service.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import net.bytecms.core.utils.Checker;
import net.bytecms.system.api.system.RoleMenuService;
import net.bytecms.system.api.system.RoleService;
import net.bytecms.system.api.system.UserRoleService;
import net.bytecms.system.dto.system.RoleDto;
import net.bytecms.system.entity.system.Role;
import net.bytecms.system.mapper.system.RoleMapper;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author dl
 * @since 2018-03-19
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleDto, Role, RoleMapper> implements RoleService {


    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleMenuService roleMenuService;

    @Override
    public ApiResult deleteById(String roleId) {
        if (roleId.equals("1")) {
            return ApiResult.result(5001);
        }
        List<String> userIds = userRoleService.selectUserIdByRId(roleId);
        if (Checker.BeNotEmpty(userIds)) {
            return ApiResult.result(5002);
        } else {
            super.removeById(roleId);
            return ApiResult.result();
        }
    }

    @Override
    public Set<String> selectRoleSignByUid(String userId) {
        Set<String> roleSigns = new HashSet<>();
        List<String> roleIds = userRoleService.selectRoleIdByUId(userId);
        List<Role> roles = (List<Role>) listByIds(roleIds);
        for (Role role : roles) {
            if (Checker.BeNotBlank(role.getRoleSign())) {
                roleSigns.add(role.getRoleSign());
            }
        }
        return roleSigns;
    }

    @Override
    public List<RoleDto> selectAllRoles() {
        QueryWrapper<Role> wrapper = new QueryWrapper<Role>();
        List<Role> roles = new ArrayList<>();
        if (!getUserId().equals("1")) {
            wrapper.notIn("role_sign", "administrator");//非超级管理员添加用户选择角色时只能添加超级管理员以外的角色
            roles = list(wrapper);
        } else {
            roles = list(wrapper);
        }
        return Checker.BeNotEmpty(roles) ? T2DList(roles) : Lists.newArrayList();
    }

}
