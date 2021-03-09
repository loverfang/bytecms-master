package net.bytecms.freemark.jobs;

import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.freemark.components.NotifyComponent;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 首页定时生成
 */
public class HomePageGenJob extends QuartzJobBean {

    @Autowired
    NotifyComponent notifyComponent;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap params=jobExecutionContext.getTrigger().getJobDataMap();
        if(Checker.BeNotNull(params)&& !params.isEmpty()){
            Object userId=params.get(SecurityConstants.USER_ID);
            if(Checker.BeNotNull(userId))
            notifyComponent.notifyHomePageManual(userId.toString());
        }else{
            notifyComponent.notifyHomePage();
        }
    }
}
