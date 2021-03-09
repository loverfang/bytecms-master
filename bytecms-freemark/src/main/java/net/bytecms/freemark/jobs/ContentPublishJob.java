package net.bytecms.freemark.jobs;

import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.freemark.enums.ContentState;
import net.bytecms.service.api.content.ContentService;
import net.bytecms.service.dto.content.ContentDto;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时生成栏目列表首页
 */
@Component
public class ContentPublishJob extends QuartzJobBean {

    @Autowired
    ContentService contentService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 业务触发执行
        JobDataMap params=jobExecutionContext.getTrigger().getJobDataMap();
        if(Checker.BeNotNull(params)&& !params.isEmpty()){
            Object contentIds=params.get("contentIds");
            Object checkUserName=params.get("checkUserName");
            Object userId=params.get(SecurityConstants.USER_ID);
            if(Checker.BeNotNull(contentIds) && contentIds instanceof  List){
                List<String> cids= ( List<String>)contentIds;
                if(Checker.BeNotEmpty(cids)){
                    ContentDto contentDto=new ContentDto();
                    contentDto.setIds(cids).setStatus(ContentState.PUBLISH.getCode());
                    if(Checker.BeNotNull(userId) && userId instanceof  String){
                        contentDto.setCheckUserId(userId.toString());
                    }
                    if(Checker.BeNotNull(checkUserName) && checkUserName instanceof  String){
                        contentDto.setCheckUserName(checkUserName.toString());
                    }
                    synchronized (contentService){
                        contentService.publish(contentDto,1);
                    }
                }
            }
        }
    }
}
