package net.bytecms.system.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bytecms.system.entity.system.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author dl
 * @since 2018-03-19
 */
@Mapper
@Repository
//@CacheNamespace(implementation=MybatisPlusRedisCache.class,eviction=MybatisPlusRedisCache.class)
public interface RoleMapper extends BaseMapper<Role> {

}
