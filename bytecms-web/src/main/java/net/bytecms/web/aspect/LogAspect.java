package net.bytecms.web.aspect;

import net.bytecms.system.api.log.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
class LogAspect {
	  
	  @Autowired
	  private LogService logService;
	
	  @Around("execution(* net.bytecms.web.controller..*.*(..)) && @annotation(net.bytecms.core.annotation.Logs)")
      public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		    Long starTime = System.currentTimeMillis();
			Object result = pjp.proceed();
		    Long time = System.currentTimeMillis()-starTime;
		    // 单位毫秒
			logService.saveLog(pjp, time.intValue());
			return result;
      }
	  
}
