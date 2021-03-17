package net.bytecms.system.service.system;

import net.bytecms.core.annotation.CacheClear;
import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.utils.BuildTree;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.system.api.system.MenuService;
import net.bytecms.system.api.system.RoleMenuService;
import net.bytecms.system.dto.system.MenuDto;
import net.bytecms.system.dto.system.RoleMenuDto;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.Tree;
import net.bytecms.system.entity.system.Menu;
import net.bytecms.system.mapper.system.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author dl
 * @since 2018-03-21
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuDto, Menu, MenuMapper> implements MenuService {

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    BaseRedisService baseRedisService;

    @CacheClear(keys={"selectPermsByUrl"})
    public boolean save(MenuDto menuDto) {
        super.insert(menuDto);
        if (Checker.BeNotBlank(menuDto.getParentId()) && !"0".equals(menuDto.getParentId())) {
            List<RoleMenuDto> roleMenuDtos = roleMenuService.getsByMenuId(menuDto.getParentId());
            if (Checker.BeNotEmpty(roleMenuDtos)) {
                roleMenuDtos.forEach(roleMenuDto -> {
                    roleMenuDto.setHalfChecked(0);
                });
                roleMenuService.updateByPks(roleMenuDtos);
                clearPermsCacheByUid(getUserId());
            }
        }
        return true;
    }

    @CacheClear(keys={"selectPermsByUrl"})
    @Override
    public boolean update(MenuDto menuDto) {
        clearPermsCacheByUid(getUserId());
        return super.updateByPk(menuDto);
    }

    @Override
    public Tree<MenuDto> selectTreeList() {
        Map<String, Object> map = new HashMap<>(16);
        List<MenuDto> menus = listDtoByMap(map);
        if (menus != null && !menus.isEmpty()) {
            List<Tree<MenuDto>> trees = new ArrayList<Tree<MenuDto>>();
            for (MenuDto sysMenuDO : menus) {
                Tree<MenuDto> tree = new Tree<MenuDto>();
                tree.setId(sysMenuDO.getId());
                tree.setKey(sysMenuDO.getId());
                tree.setParentId(sysMenuDO.getParentId());
                tree.setName(sysMenuDO.getName());
                tree.setSpread(true);
                Map<String, Object> attributes = new HashMap<>(16);
                attributes.put("url", sysMenuDO.getUrl());
                attributes.put("icon", sysMenuDO.getIcon());
                attributes.put("perms", sysMenuDO.getPerms());
                attributes.put("type", sysMenuDO.getType());
                attributes.put("orderNum", sysMenuDO.getOrderNum());
                tree.setAttributes(attributes);
                trees.add(tree);
            }
            Tree<MenuDto> t = BuildTree.build(trees);
            return t;
        } else {
            return null;
        }
    }

    @Override
    public List<MenuDto> selectMenuTreeByUid(String userId) {
        return baseMapper.selectMenuTreeByUid(userId);
    }


    @Cacheable(value= Constants.cacheName, key="#root.targetClass+'.'+#root.methodName+'.'+#p0",unless="#result == null")
    @Override
    public Set<String> selectPermsByUid(String userId) {
        List<String> perms = baseMapper.selectPermsByUid(userId);
        Set<String> permsSet = new HashSet<>();
        if (Checker.BeNotEmpty(perms)) {
            for (String perm : perms) {
                if (Checker.BeNotBlank(perm)) {
                    permsSet.addAll(Arrays.asList(perm.trim().split(",")));
                }
            }
        }
        permsSet.add(SecurityConstants.NOT_REQUIRED_HAVE_PERM);
        return permsSet;
    }

    @Override
    public void clearPermsCacheByUid(String userId) {
        String key = Constants.cacheNameClear+getClass().toString()+".selectPermsByUid."+Constants.delRedisKey;
        baseRedisService.removeBlear(key);
    }


    @Cacheable(value= Constants.cacheName, key="#root.targetClass+'.'+#root.methodName+'.'+#p0",unless="#result == null")
    @Override
    public Set<String> selectPermsByUrl(String url) {
        List<String> perms = baseMapper.selectPermsByUrl(url);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (Checker.BeNotBlank(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<MenuDto> selectMenuUid(String userId) {
        return baseMapper.selectMenuUid(userId);
    }

    @Override
    public MenuDto info(String id) {
        MenuDto chileMenuDto = getByPk(id);
        MenuDto parentMenuDto = getByPk(chileMenuDto.getParentId());
        if (Checker.BeNotNull(parentMenuDto)) {
            chileMenuDto.setParentName(parentMenuDto.getName());
        } else {
            chileMenuDto.setParentName("根目录");
        }
        return chileMenuDto;
    }

    @CacheClear(keys = {"selectPermsByUid"})
    @Override
    public boolean delete(String id) {
        MenuDto menu = getByPk(id);
        //目录或者菜单需要递归删除
        if (menu.getType() == 0 || menu.getType() == 1) {
            List<String> ids = new ArrayList<>();
            ids.add(id);
            selectChilden(id, ids);
            boolean f = super.removeByIds(ids);
            //删除菜单或者目录成功时将删除，角色-菜单关系表
            if (f) {
                for (String menuId : ids) {
                    roleMenuService.deleteByMenuId(menuId);
                }
                return true;
            }
        } else if (menu.getType() == 2){
            //按钮
            return super.removeById(id);
        }
        return false;
    }

    private void selectChilden(String id, List<String> list) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("parent_id", id);
        List<MenuDto> menus = listDtoByMap(param);
        if (null != menus && !menus.isEmpty()) {
            for (MenuDto menu : menus) {
                list.add(menu.getId());
                selectChilden(menu.getId(), list);
            }
        }
    }

}
