package net.bytecms.web.controller.job;

import net.bytecms.core.annotation.Logs;
import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.enumerate.LogModule;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.freemark.corelibs.job.JobExecute;
import net.bytecms.freemark.enums.JobActionNotify;
import net.bytecms.freemark.jobs.ContentClickAndGiveLikeJob;
import net.bytecms.service.api.category.CmsCategoryService;
import net.bytecms.service.api.content.ContentService;
import net.bytecms.service.api.fragment.FragmentService;
import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.web.controller.BaseController;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("job")
public class JobController extends BaseController<FragmentService> {

    @Autowired
    JobExecute jobExecute;

    @Autowired
    CmsCategoryService cmsCategoryService;

    @Autowired
    ContentService contentService;

    @Autowired
    BaseRedisService baseRedisService;

    @Autowired
    ContentClickAndGiveLikeJob contentClickAndGiveLikeJob;


    @Logs(module = LogModule.JOB,operation = "生成首页")
    @GetMapping(value="homePageGen")
    public void reStaticFileByCid(){
        Map<String,Object> param = new HashMap<>();
        param.put(SecurityConstants.USER_ID,getUserId());
        jobExecute.execute(JobActionNotify.JOB_HOME_PAGE_RIGHT_NOW.setSecond(2),param);
    }


    @Logs(module = LogModule.JOB,operation = "全站生成")
    @GetMapping(value="reStaticWholeSite")
    public void reStaticWholeSite(){
        List<CmsCategoryDto> categoryDtos=cmsCategoryService.selectCategoryForWholeSiteGen();
        Map<String,Object> param = new HashMap<>();
        param.put(SecurityConstants.USER_ID,getUserId());
        param.put("categorys",categoryDtos);
        jobExecute.execute(JobActionNotify.JOB_WHOLE_SITE_RIGHT_NOW.setSecond(2),param);
    }


    @Logs(module = LogModule.JOB,operation = "清空缓存")
    @PutMapping(value="flushDB")
    public void flushDB(){
        try {
            contentClickAndGiveLikeJob.executeInternal(null);
        } catch (JobExecutionException e) {
            e.printStackTrace();
        }
        baseRedisService.flushDB();
    }



    @Logs(module = LogModule.JOB,operation = "重置索引库")
    @PutMapping(value="reContentToSolrCore")
    public void reContentToSolrCore(){
        Map<String,Object> param = new HashMap<>();
        param.put(SecurityConstants.USER_ID,getUserId());
        jobExecute.execute(JobActionNotify.JOB_SOLR_SYNC_DATA_RIGHT_NOW.setSecond(2).setMinute(0),param,true);
    }

    @Logs(module = LogModule.JOB,operation = "生成指定模板")
    @PutMapping(value="genFixedTemp")
    public void genFixedTemp(@RequestParam Map<String,Object> param){
        if(Checker.BeNull(param)||param.isEmpty()){
            throw new CustomException(ApiResult.result(20021));
        }
        param.put(SecurityConstants.USER_ID,getUserId());
        jobExecute.execute(JobActionNotify.JOB_FIXED_TEMPLATE_RIGHT_NOW.setSecond(2).setMinute(0),param,true);
    }

}
