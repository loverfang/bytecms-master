package net.bytecms.freemark.jobs;

import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.freemark.components.NotifyComponent;
import net.bytecms.service.dto.content.ContentDto;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 定时生成栏目列表首页
 */
@Component
public class SolrSyncDataJob extends QuartzJobBean {

    @Autowired
    NotifyComponent notifyComponent;


    private List<ContentDto> contentDtos =new ArrayList<>();

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 业务触发执行
        JobDataMap params=jobExecutionContext.getTrigger().getJobDataMap();
        String uid = "";
        if(Checker.BeNotNull(params)&& !params.isEmpty()){
            Object userId=params.get(SecurityConstants.USER_ID);
            if(Checker.BeNotNull(userId) && userId instanceof  String){
                uid = userId.toString();
            }
        }
        notifyComponent.notifyResetSolr( uid);
    }
}
