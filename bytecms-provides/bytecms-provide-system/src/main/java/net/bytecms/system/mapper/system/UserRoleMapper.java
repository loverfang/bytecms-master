package net.bytecms.system.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bytecms.system.entity.system.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户与角色对应关系 Mapper 接口
 * </p>
 *
 * @author dl
 * @since 2018-03-23
 */
@Mapper
@Repository
//@CacheNamespace(implementation=MybatisPlusRedisCache.class,eviction=MybatisPlusRedisCache.class)
public interface UserRoleMapper extends BaseMapper<UserRole> {

	String selectRoleName(Long userId);

	List<String> selectRoleSign(Long userId);
}
