package net.bytecms.system.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bytecms.system.dto.system.MenuDto;
import net.bytecms.system.entity.system.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author dl
 * @since 2018-03-21
 */
@Mapper
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

	List<MenuDto> selectMenuTreeByUid(String userId);

	List<String> selectPermsByUid(String userId);

	List<MenuDto> selectMenuUid(String userId);

	List<String> selectPermsByUrl(@Param("url") String url);
}
