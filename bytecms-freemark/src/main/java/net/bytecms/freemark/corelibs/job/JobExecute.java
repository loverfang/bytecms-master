package net.bytecms.freemark.corelibs.job;

import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.JobUtil;
import net.bytecms.core.utils.PasswordGenerator;
import net.bytecms.freemark.enums.JobActionNotify;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JobExecute extends AbsJobExecute {

    @Autowired
    Scheduler scheduler;

    @Override
    public void execute(JobActionNotify jobActionNotify) {
        if (!jobIsExist(scheduler, jobActionNotify)) {
            synchronized (this) {
                JobUtil.createJobByStartAt(scheduler, executeTime(jobActionNotify.getMinute()), jobActionNotify.getName(), jobActionNotify.getGroup(), jobActionNotify.getJobClass());
            }
        }
    }

    @Override
    public void execute(JobActionNotify jobActionNotify, Map<String, Object> param) {
        synchronized (this) {
            Trigger job = jobTrigger(scheduler, jobActionNotify);
            if (Checker.BeNull(job)) {
                long executeDate = executeTime(jobActionNotify);
                JobUtil.createJobByStartAt(scheduler, executeDate, jobActionNotify.getName(), jobActionNotify.getGroup(), jobActionNotify.getJobClass(), param);
            }
        }
    }

    @Override
    public void execute(JobActionNotify jobActionNotify, JobExecuteHandler jobExecuteHandler, Map<String, Object> param) {
        synchronized (this) {
            Trigger job = jobTrigger(scheduler, jobActionNotify);
            boolean iscontinue= jobExecuteHandler.before(scheduler,job,param);
            if(iscontinue){
                long executeDate = executeTime(jobActionNotify);
                JobUtil.createJobByStartAt(scheduler, executeDate, jobActionNotify.getName(), jobActionNotify.getGroup(), jobActionNotify.getJobClass(), param);
            }
            jobExecuteHandler.after(scheduler,job,param);
        }
    }

    @Override
    public void execute(JobActionNotify jobActionNotify, Map<String, Object> param, boolean forceExecute) {
        if (forceExecute) {
            jobActionNotify.setName(jobActionNotify.getName() + PasswordGenerator.genPass(5));
        }
        execute(jobActionNotify, param);
    }

}
