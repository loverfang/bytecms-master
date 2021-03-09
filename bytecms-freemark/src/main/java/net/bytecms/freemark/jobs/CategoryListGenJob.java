package net.bytecms.freemark.jobs;

import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.freemark.components.NotifyComponent;
import net.bytecms.service.api.category.CmsCategoryService;
import net.bytecms.service.dto.category.CmsCategoryDto;
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
public class CategoryListGenJob extends QuartzJobBean {

    @Autowired
    NotifyComponent notifyComponent;

    @Autowired
    CmsCategoryService cmsCategoryService;

    private List<CmsCategoryDto> cmsCategoryDtos =new ArrayList<>();

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 业务触发执行
        JobDataMap params=jobExecutionContext.getTrigger().getJobDataMap();
        JobDataMap jobData=jobExecutionContext.getJobDetail().getJobDataMap();
        String uid = "";
        if(Checker.BeNotNull(params)&& !params.isEmpty()){
            Object categorys=params.get("categorys");
            Object userId=params.get(SecurityConstants.USER_ID);
            if(Checker.BeNotNull(categorys) && categorys instanceof  List){
                List<CmsCategoryDto> cats= ( List<CmsCategoryDto>)categorys;
                if(Checker.BeNotEmpty(cats)){
                    this.cmsCategoryDtos = cats;
                }
            }
            if(Checker.BeNotNull(userId) && userId instanceof  String){
                uid = userId.toString();
            }
        }
        //  定时计划执行
        if(Checker.BeNotNull(jobData) && !jobData.isEmpty()){
            Object code=jobData.get("param");
            if(Checker.BeNotNull(code)){
                List<CmsCategoryDto> cmsCategorys = cmsCategoryService.listByField("code",code.toString());
                if(Checker.BeNotEmpty(cmsCategorys)){
                    this.cmsCategoryDtos = cmsCategorys;
                }
            }
        }
        notifyComponent.notifyReGenCategory(cmsCategoryDtos,uid);
    }
}
