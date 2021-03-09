package net.bytecms.system.mapper.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bytecms.system.entity.log.Log;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统日志 Mapper 接口
 * </p>
 *
 * @author lgs
 * @since 2019-08-12
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

}
