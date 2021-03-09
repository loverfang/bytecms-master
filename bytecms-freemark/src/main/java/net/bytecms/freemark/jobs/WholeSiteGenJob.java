package net.bytecms.freemark.jobs;

import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.freemark.components.NotifyComponent;
import net.bytecms.service.dto.category.CmsCategoryDto;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * 全站生成
 */
public class WholeSiteGenJob extends QuartzJobBean {

    @Autowired
    NotifyComponent notifyComponent;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap params=jobExecutionContext.getTrigger().getJobDataMap();
        if(Checker.BeNotNull(params)&& !params.isEmpty()){
            Object userId=params.get(SecurityConstants.USER_ID);
            Object categorys=params.get("categorys");
            if(Checker.BeNotNull(userId)&&Checker.BeNotNull(categorys)){
                List<CmsCategoryDto> category = (List<CmsCategoryDto>) categorys;
                notifyComponent.notifyWholeSite(category,userId.toString());
            }
        }
    }
}
