package net.bytecms.freemark.corelibs.job;

import org.quartz.Scheduler;
import org.quartz.Trigger;

import java.util.Map;

public abstract class JobExecuteHandler {


    public abstract boolean before(Scheduler scheduler,Trigger job, Map<String, Object> param);

    public abstract void after(Scheduler scheduler,Trigger job, Map<String, Object> param);


}
