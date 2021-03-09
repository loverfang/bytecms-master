package net.bytecms.freemark.jobs;

import net.bytecms.core.utils.Checker;
import net.bytecms.system.api.log.LogService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 定时清理日志记录
 */
@Component
public class ClearLogJob extends QuartzJobBean {

    @Autowired
    LogService logService;

    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap params=jobExecutionContext.getTrigger().getJobDataMap();
        JobDataMap jobData=jobExecutionContext.getJobDetail().getJobDataMap();
        boolean ckIsLegal= Checker.BeNotNull(jobData) && !jobData.isEmpty() && jobData.containsKey("param") &&
        Checker.BeNotNull(jobData.get("param")) ;
        if(ckIsLegal){
            Integer month =  Integer.valueOf(jobData.get("param").toString());
            logService.deleteLogBeforeMonth(month);
        }
    }
}
