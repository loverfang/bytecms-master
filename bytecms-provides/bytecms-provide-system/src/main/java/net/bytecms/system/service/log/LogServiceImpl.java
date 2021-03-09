package net.bytecms.system.service.log;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.bytecms.core.utils.Checker;
import net.bytecms.system.api.log.LogService;
import net.bytecms.system.dto.log.LogDTO;
import net.bytecms.system.mapper.log.LogMapper;
import net.bytecms.core.utils.BaseContextKit;
import net.bytecms.core.utils.HttpContextUtils;
import net.bytecms.core.utils.RequestUtils;
import net.bytecms.core.annotation.Logs;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.system.entity.log.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author lgs
 * @since 2019-08-12
 */
@Transactional
@Service
public class LogServiceImpl extends BaseServiceImpl<LogDTO, Log, LogMapper> implements LogService {

    protected List<String> select() {
        return null;
    }

    protected QueryWrapper<Log> condition(LogDTO v) {
        return null;
    }

    @Override
    public void saveLog(ProceedingJoinPoint joinPoint, int time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Date date = new Date();
        Method method = signature.getMethod();
        LogDTO sysLog = new LogDTO();
        Logs logs = method.getAnnotation(Logs.class);
        if (logs != null) {
            String opera=logs.operaEnum().getCode()+logs.operation();
            sysLog.setOperation(opera);
            sysLog.setModule(logs.module().getName());
            sysLog.setType(logs.module().getCode());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");
        // 请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            if(Checker.BeNotEmpty(args)){
                String params="";
                for(Object o:args){
                    if(Checker.BeNotNull(o) && Checker.BeNotBlank(o.toString())){
                        params += JSON.toJSONString(o)+"|";
                    }
                }
                if(Checker.BeNotBlank(params)&&params.endsWith("|"))
                params=params.substring(0,params.length()-1);
                sysLog.setParams(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        // 设置IP地址
        sysLog.setIp(RequestUtils.getIpAddr(request));
        // 用户名
        String userId = BaseContextKit.getUserId();
        String userName = BaseContextKit.getUserName();
        String name = BaseContextKit.getName();
        if (Checker.BeNotBlank(userId)) {
            sysLog.setUserId(userId);
        }
        if (Checker.BeNotBlank(userName)) {
            sysLog.setUsername(userName);
        }
        if (Checker.BeNotBlank(name)) {
            sysLog.setName(name);
        }
        sysLog.setTime(time).setId(generateId());
        insert(sysLog);
    }

    @Transactional
    @Override
    public void deleteLogBeforeMonth(Integer month) {
        if(month!=null){
            Date date = DateUtil.date();
            Date newDate = DateUtil.offset(date, DateField.MONTH, -month);
            QueryWrapper<Log> queryWrapper=new QueryWrapper();
            queryWrapper.lt("gmt_create",newDate);
            baseMapper.delete(queryWrapper);
        }
    }

}
