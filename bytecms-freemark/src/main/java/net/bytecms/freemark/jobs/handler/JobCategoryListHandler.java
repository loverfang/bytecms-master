package net.bytecms.freemark.jobs.handler;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.JobUtil;
import net.bytecms.freemark.corelibs.job.JobExecuteHandler;
import net.bytecms.service.dto.category.CmsCategoryDto;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobCategoryListHandler extends JobExecuteHandler {

    @Override
    public boolean before( Scheduler scheduler,Trigger trigger, Map<String, Object> param) {
        // 判断触发器是否需要重置
        if(Checker.BeNotNull(trigger)){
            String categorys = Constants.categorys;
            JobDataMap jobDataMap = trigger.getJobDataMap();
            List<CmsCategoryDto> needAdd = new ArrayList<>();
            boolean needCk = Checker.BeNotNull(jobDataMap) && !jobDataMap.isEmpty()
            && Checker.BeNotNull(param) && !param.isEmpty() && param.containsKey(categorys);
            if (needCk) {
                if (jobDataMap.containsKey(categorys)) {
                    Object paramTriggerObj = jobDataMap.get(categorys);
                    Object paramJobObj = param.get(categorys);
                    if (Checker.BeNotNull(paramTriggerObj) && Checker.BeNotNull(paramJobObj)) {
                        List<CmsCategoryDto> triggerCategoryDtos = (List<CmsCategoryDto>) paramTriggerObj;
                        List<CmsCategoryDto> jobCategoryDtos = (List<CmsCategoryDto>) paramJobObj;
                        if (Checker.BeNotEmpty(triggerCategoryDtos) && Checker.BeNotEmpty(jobCategoryDtos)) {
                            jobCategoryDtos.forEach(jobCategoryDto -> {
                                boolean contains = false;
                                for (CmsCategoryDto triggerCategoryDto : triggerCategoryDtos) {
                                    if (triggerCategoryDto.getId().equals(jobCategoryDto.getId())) {
                                        contains = true;
                                    }
                                }
                                if (!contains) {
                                    needAdd.add(jobCategoryDto);
                                }
                            });
                            if (Checker.BeNotEmpty(needAdd)) {
                                triggerCategoryDtos.addAll(needAdd);
                                Map<String, Object> params = new HashMap<>();
                                params.put(categorys, triggerCategoryDtos);
                                JobUtil.modifyJobParam(scheduler, trigger, params);
                            }
                        }
                    }
                }

            }
        }
        return Checker.BeNull(trigger);
    }

    @Override
    public void after(Scheduler scheduler, Trigger job, Map<String, Object> param) {

    }
}
