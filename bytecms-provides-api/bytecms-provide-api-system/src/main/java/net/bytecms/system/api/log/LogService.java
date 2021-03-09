package net.bytecms.system.api.log;

import net.bytecms.core.api.BaseService;
import net.bytecms.system.dto.log.LogDTO;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author lgs
 * @since 2019-08-12
 */
public interface LogService extends BaseService<LogDTO> {


    void saveLog(ProceedingJoinPoint pjp, int l);

    void deleteLogBeforeMonth(Integer month);
}
