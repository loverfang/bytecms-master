package net.bytecms.web.config;

import net.bytecms.core.utils.JobUtil;
import net.bytecms.freemark.jobs.HardWareMonitorJob;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HardWareMonitorConfig implements CommandLineRunner {

    @Autowired
    Scheduler scheduler;

    @Override
    public void run(String... args) throws Exception {
        JobUtil.createJobByCron(scheduler,"HardWareMonitor","group5","0/3 * * * * ?", HardWareMonitorJob.class);
       //  JobUtil.createJobByCron(scheduler,"LegalVerifyJob1","group5","0 15 10 15 * ?", LegalVerifyJob.class);
    }
}
