package net.bytecms.freemark.jobs;
import com.alibaba.fastjson.JSON;
import net.bytecms.core.utils.HardWareMonitor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HardWareMonitorJob extends QuartzJobBean {

    @Autowired
    SimpMessagingTemplate SMT;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String,Object> hardInfo= HardWareMonitor.hardWareInfo();
        SMT.convertAndSend("/topic/hardWareMonitor", JSON.toJSONString(hardInfo));
    }
}
