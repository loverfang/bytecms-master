package net.bytecms.system.service.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import net.bytecms.core.utils.BuildTree;
import net.bytecms.core.utils.Checker;
import net.bytecms.system.api.system.MenuService;
import net.bytecms.system.api.system.RoleMenuService;
import net.bytecms.system.dto.system.MenuDto;
import net.bytecms.system.dto.system.RoleMenuDto;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.Tree;
import net.bytecms.system.entity.system.RoleMenu;
import net.bytecms.system.mapper.system.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色与菜单对应关系 服务实现类
 * </p>
 *
 * @author dl
 * @since 2018-03-23
 */
@Service
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenuDto, RoleMenu, RoleMenuMapper> implements RoleMenuService {

    @Autowired
    MenuService menuService;

    @Override
    public Tree<MenuDto> selectTreeMenuByUser(String roleId, String userId) {
        //给菜单分配权限时，首先查询该用户是否是超级管理员（id=1） ,超级管理员查询全部菜单，非超级管理员值查询自己拥有的权限
        Map<String, Object> param = new HashMap<>();
        int k = 0;
        List<MenuDto> menus = new ArrayList<>();
        //查询所有的菜单
        if (userId.equals("1")) {
            menus = menuService.listDtoByMap(param);
        } else {
            menus = menuService.selectMenuUid(userId);
        }
        //查询当前角色所拥有的菜单用于编辑时还原勾选状态
        List<String> menuIds = baseMapper.selectMenus(roleId);
        //param.put("role_id", roleId);
        //查询当前角色所拥有的菜单用于编辑时还原勾选状态
        //List<String> menuIds=selectMenus(param);
        List<Tree<MenuDto>> trees = new ArrayList<Tree<MenuDto>>();
        for (MenuDto menu : menus) {
            String typeName = "";
            if (menu.getType() == 0) {typeName = "(目录)";}
            if (menu.getType() == 1) {typeName = "(菜单)";}
            if (menu.getType() == 2) {typeName = "(按钮)";}
            Tree<MenuDto> tree = new Tree<MenuDto>();
            tree.setKey(menu.getId());
            tree.setId(menu.getId());
            tree.setParentId(menu.getParentId());
            tree.setTitle(menu.getName() + typeName);
            trees.add(tree);
        }
        List<Tree<MenuDto>> topNodes = BuildTree.buildList(trees, "0");
        Tree<MenuDto> root = new Tree<MenuDto>();
        if (topNodes.size() == 1) {
            root = topNodes.get(0);
        } else {
            root.setKey("-1");
            root.setId("-1");
            root.setParentId("0");
            root.setHasParent(false);
            root.setHasChildrenNode(true);
            root.setChildren(topNodes);
            root.setTitle("顶级节点");
        }
        if (Checker.BeNotEmpty(menuIds)) {
            Map<String, Object> attr = new HashMap<>(16);
            attr.put("checkerKeys", menuIds);
            root.setAttributes(attr);
        }
        return root;
    }

    private List<String> selectMenus(Map<String, Object> param) {
        List<String> menuIds = new ArrayList<>();
        List<RoleMenu> menus = (List<RoleMenu>) listByMap(param);
        if (!menus.isEmpty()) {
            for (RoleMenu menu : menus) {
                menuIds.add(menu.getMenuId());
            }
        }
        return menuIds;
    }

    @Transactional
    @Override
    public boolean assignMenu(RoleMenuDto roleMenuDto) {
        menuService.clearPermsCacheByUid(getUserId());
        //半选中
        List<String> halfCheckedKeys = roleMenuDto.getHalfCheckedKeys();
        //全选中
        List<String> onCheckKeys = roleMenuDto.getOnCheckKeys();
        String roleId = roleMenuDto.getRoleId();
        deleteByRoleId(roleId);
        //全部删除
        if (onCheckKeys.size() == 1 && onCheckKeys.get(0).equals("-1")) {
            return true;
        } else {
            List<RoleMenuDto> halfList = filterList(halfCheckedKeys, roleId, 0);
            List<RoleMenuDto> checkList = filterList(onCheckKeys, roleId, 1);
            List<RoleMenuDto> allList = new ArrayList<>();
            allList.addAll(halfList);
            allList.addAll(checkList);
            return insertBatch(allList);
        }
    }

    private List<RoleMenuDto> filterList(List<String> keys, String roleId, Integer halfChecked) {
        List<RoleMenuDto> roleMenus = new ArrayList<>();
        for (String menuId : keys) {
            if (menuId.equals("-1")) {continue;}
            RoleMenuDto roleMenu = new RoleMenuDto();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenu.setId(generateId());
            roleMenu.setHalfChecked(halfChecked);
            roleMenus.add(roleMenu);
        }
        return roleMenus;
    }

    @Override
    public boolean deleteByRoleId(String roleId) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("role_id", roleId);
        return removeByMap(param);
    }

    @Override
    public boolean deleteByMenuId(String menuId) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("menu_id", menuId);
        return removeByMap(param);
    }

    @Override
    public List<RoleMenuDto> getsByMenuId(String menuId) {
        List<String> menuIds = new ArrayList<>();
        menuIds.add(menuId);
        MenuDto menuDto = menuService.getByPk(menuId);
        if (menuDto.getType() == 1) {//菜单类型的话查询目录s
            String mid = (Checker.BeNotBlank(menuDto.getParentId()) && !"0".equals(menuDto.getParentId())) ? menuDto.getParentId() : "-1";
            MenuDto pmenuDto = menuService.getByPk(mid);
            if (Checker.BeNotNull(pmenuDto)) {
                menuIds.add(pmenuDto.getId());
            }
        }
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<RoleMenu>();
        queryWrapper.in("menu_id", menuIds);
        List<RoleMenu> list = baseMapper.selectList(queryWrapper);
        if (Checker.BeNotEmpty(list)) {
            return T2DList(list);
        }
        return Lists.newArrayList();
    }

}
