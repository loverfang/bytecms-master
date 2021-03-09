package net.bytecms.system.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bytecms.system.entity.system.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色与菜单对应关系 Mapper 接口
 * </p>
 *
 * @author dl
 * @since 2018-03-23
 */
@Mapper
@Repository
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    @Select("select srm.menu_id as menuIds from sys_role_menu srm where srm.role_id=#{roleId} and srm.half_checked in (1) ")
    List<String> selectMenus(@Param("roleId") String roleId);
}
