package net.bytecms.freemark.jobs;

import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;

import net.bytecms.system.api.system.UserService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Map;
@Slf4j
@Component
public class LegalVerifyJob extends QuartzJobBean {

    @Autowired
    BaseRedisService baseRedisService;

    @Autowired
    UserService userService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Object object=baseRedisService.get(SecurityConstants.LICENSE_NAME);
        if(object==null){
            Object loginDate=baseRedisService.get(SecurityConstants.LOGIN_DATE_FRIST);
            if(Checker.BeNotNull(loginDate)){
                log.error("20028:license证书不合法");
                userService.lockUsers(false);
            }
        }else{
            Map<String,Object> objectMap= ( Map<String,Object>)object;
            verify(objectMap);
        }
    }
    private void verify(Map<String,Object> objectMap){
        if(!objectMap.isEmpty()){
            String sign=objectMap.get(SecurityConstants.SIGNATURER).toString();
            objectMap.remove(SecurityConstants.SIGNATURER);
            boolean valid = false;
            try {
                // valid = VerifyLicenseUtil.verify(objectMap.toString().getBytes(), SecurityConstants.PUBLIC_KEY, sign);
            } catch (Exception e) {
                log.error("20028:license证书不合法");
                userService.lockUsers(true);
            }
            if(!valid){
                log.error("20028:license证书不合法");
                userService.lockUsers(true);
            }
        }
    }
}
