package net.bytecms.system.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bytecms.system.entity.system.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<UserEntity> {

    Set<String> selectRoleSignByUserId(@Param("userId") String uid);

    boolean lockUsers(@Param("pass") String pass,@Param("justLock") Boolean justLock);
}
