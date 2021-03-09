package net.bytecms.freemark.corelibs.job;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.JobUtil;
import net.bytecms.freemark.enums.JobActionNotify;
import org.quartz.Scheduler;
import org.quartz.Trigger;

import java.util.Date;
import java.util.Map;

public abstract class AbsJobExecute {

    protected abstract void execute(JobActionNotify jobActionNotify);

    protected abstract void execute(JobActionNotify jobActionNotify, Map<String, Object> param);

    protected abstract void execute(JobActionNotify jobActionNotify, JobExecuteHandler jobExecuteHandler, Map<String, Object> param);

    protected abstract void execute(JobActionNotify jobActionNotify, Map<String, Object> param,boolean forceExecute);

    protected long executeTime(int afterMin){
        Date date = DateUtil.date();
        Date newDate = DateUtil.offset(date, DateField.MINUTE, afterMin);
        return newDate.getTime();
    }

    protected long executeTime(JobActionNotify jobActionNotify){
        // 默认以分钟为准，存在秒 则以秒为准，存在具体时间则以具体时间为准
        long executeDate = executeTime(jobActionNotify.getMinute());
        if (jobActionNotify.getSecond() != 0) {
            executeDate = executeTime(jobActionNotify.getMinute(), jobActionNotify.getSecond());
        }
        if(Checker.BeNotNull(jobActionNotify.getPreciseTime())){
            executeDate=jobActionNotify.getPreciseTime().getTime();
        }
        return executeDate;
    }

    protected long executeTime(int afterMin,int afterSecond){
        if(afterMin!=0){
           return executeTime(afterMin);
        }else{
            Date date = DateUtil.date();
            Date newDate = DateUtil.offset(date, DateField.SECOND, afterSecond);
            return newDate.getTime();
        }
    }

    protected boolean jobIsExist(Scheduler scheduler,JobActionNotify jobActionNotify){
        Trigger trigger= JobUtil.ckJobIsExist(scheduler,jobActionNotify.getName(),jobActionNotify.getGroup());
        return Checker.BeNotNull(trigger);
    }

    protected Trigger jobTrigger(Scheduler scheduler,JobActionNotify jobActionNotify){
        Trigger trigger=JobUtil.ckJobIsExist(scheduler,jobActionNotify.getName(),jobActionNotify.getGroup());
        return trigger;
    }
}
