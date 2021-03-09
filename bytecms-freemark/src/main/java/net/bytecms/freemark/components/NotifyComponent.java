package net.bytecms.freemark.components;

import com.alibaba.fastjson.JSON;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.freemark.corelibs.job.JobExecute;
import net.bytecms.freemark.corelibs.notify.AbstractNotify;
import net.bytecms.freemark.corelibs.observer.Observer;
import net.bytecms.freemark.corelibs.observer.ObserverAction;
import net.bytecms.freemark.corelibs.observer.ObserverData;
import net.bytecms.freemark.corelibs.observer.data.ContentCreateObserverData;
import net.bytecms.freemark.corelibs.observer.data.ContentPublishObserverData;
import net.bytecms.freemark.corelibs.observer.data.HomePageObserverData;
import net.bytecms.freemark.corelibs.observer.data.SolrObserverData;
import net.bytecms.freemark.enums.JobActionNotify;
import net.bytecms.freemark.jobs.handler.JobCategoryListHandler;
import net.bytecms.freemark.observers.*;
import net.bytecms.freemark.subjects.CategorySubject;
import net.bytecms.freemark.subjects.HomePageSubject;
import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.service.dto.content.ContentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 通知组件
 */
@Slf4j
@Component
public class NotifyComponent extends AbstractNotify {

    @Autowired
    CategorySubject categorySubject;

    @Autowired
    HomePageSubject homePageSubject;

    @Autowired
    ContentAllObserver contentAllObserver;

    @Autowired
    ContentListObserver contentListObserver;

    @Autowired
    HomePageObserver homePageObserver;

    @Autowired
    DynamicPageObserver dynamicPageObserver;

    @Autowired
    SolrObserver solrObserver;

    @Autowired
    private JobExecute jobExecute;

    @Autowired
    SimpMessagingTemplate SMT;

    /**
     * 通知文章创建
     * @param observer
     * @param contentDto
     * @param callBack
     * @param userId
     */
    public void notifyCreate(Observer observer, ContentDto contentDto, CmsCategoryDto categoryDto, boolean callBack, String userId){
        ContentCreateObserverData contentObserverData=new ContentCreateObserverData(ObserverAction.CONTENT_CREATE);
        contentObserverData.setContentDto(contentDto).setCategoryDto(categoryDto).buildParams();
        if(Checker.BeNotBlank(userId)){
            contentObserverData.setCreateId(userId);
        }
        notify(categorySubject,observer,contentObserverData,callBack);
    }

    /**
     * 批量更新文章
     * @param contentDtos
     */
    public void notifyCreates(Observer observer, List<ContentDto> contentDtos, String userId){
        if(Checker.BeNotEmpty(contentDtos)){
            for(ContentDto contentDto :contentDtos){
               notifyCreate(observer,contentDto,null,true,userId);
            }
        }
    }

    /**
     * 通知文章发布,挂起到分类页面
     * @param cmsCategoryDtos
     */
    public void notifyPublish(List<CmsCategoryDto> cmsCategoryDtos,String userId){
        ContentPublishObserverData contentPublish = new ContentPublishObserverData(ObserverAction.CONTENT_PUBLISH);
        contentPublish.setCmsCategoryDtos(cmsCategoryDtos);
        notify(categorySubject,contentListObserver,contentPublish,Checker.BeNotBlank(userId));
    }

    /**
     * 重新生成 分类页面 手动触发
     * @param cmsCategoryDtos
     * @param userId
     */
    public void notifyReGenCategory(List<CmsCategoryDto> cmsCategoryDtos, String userId){
        if(Checker.BeBlank(userId)){
            notifyPublish(cmsCategoryDtos,userId);
        }else{
            ContentPublishObserverData contentPublish = new ContentPublishObserverData(ObserverAction.CATEGORY_CHANGE);
            contentPublish.setCmsCategoryDtos(cmsCategoryDtos);
            contentPublish.setCreateId(userId);
            notify(categorySubject,contentListObserver,contentPublish,true);
        }
    }
    /**
     * 通知更新首页
     */
    public void notifyHomePage(){
        HomePageObserverData homePageObserverData =new HomePageObserverData(ObserverAction.HOME_PAGE_CHANGE);
        notify(homePageSubject, homePageObserver,homePageObserverData,false);
    }


    /**
     * 通知同步索引库
     */
    public void notifyResetSolr(String userId){
        SolrObserverData solrObserverData =new SolrObserverData(ObserverAction.SOLR_CHANGE);
        solrObserverData.setCreateId(userId);
        notify(homePageSubject,solrObserver,solrObserverData,true);
    }


    /**
     * 手动通知更新指定模板
     */
    public void notifyFixedTemplate(String userId,Map<String,Object> param){
        HomePageObserverData homePageObserverData =new HomePageObserverData(ObserverAction.HOME_PAGE_CHANGE);
        homePageObserverData.setCreateId(userId);
        homePageObserverData.setMapData(param);
        notify(homePageSubject,dynamicPageObserver,homePageObserverData,true);
    }


    /**
     * 手动通知更新首页
     */
    public void notifyHomePageManual(String userId){
        HomePageObserverData homePageObserverData =new HomePageObserverData(ObserverAction.HOME_PAGE_CHANGE);
        homePageObserverData.setCreateId(userId);
        notify(homePageSubject, Arrays.asList(homePageObserver),homePageObserverData,true);
    }


    /**
     * 通知更新指定栏目下的 文章的内容详情
     */
    public void notifyAllContent(String userId){
        ContentCreateObserverData contentObserverData=new ContentCreateObserverData(ObserverAction.CONTENT_CREATE);
        contentObserverData.setCreateId(userId);
        notify(categorySubject,contentAllObserver,contentObserverData,true);
    }



    /**
     * 手动通知更新全站
     */
    public void notifyWholeSite(List<CmsCategoryDto> cmsCategoryDtos,String userId){
        notifyHomePageManual(userId);
        notifyReGenCategory(cmsCategoryDtos,userId);
        notifyAllContent(userId);
    }

    public void  asyncNotifyHomePage(int minute,int second){
        synchronized (jobExecute){
            jobExecute.execute(JobActionNotify.JOB_HOME_PAGE.setMinute(minute).setSecond(second));
        }
    }

    public void  asyncNotifyContentPublish(JobActionNotify jobActionNotify,Map<String,Object> param){
        synchronized (jobExecute){
            jobExecute.execute(jobActionNotify,param);
        }
    }

    public void  asyncNotifyBatchContentGen(int minute,int second,String userId,List<ContentDto> contentDtos){
        synchronized (jobExecute){
            if(Checker.BeNotEmpty(contentDtos)){
                Map<String,Object> param = new HashMap<>();
                param.put("contents",contentDtos);
                param.put(SecurityConstants.USER_ID,userId);
                jobExecute.execute(JobActionNotify.JOB_CONTENT_LIST_RIGHT_NOW.setSecond(second).setMinute(minute),param);
            }
        }
    }


    public void  asyncNotifyCategory(int minute,int second,List<CmsCategoryDto> categoryDtos){
        synchronized (jobExecute){
            if(Checker.BeNotEmpty(categoryDtos)){
                Integer sec=categoryDtos.get(0).getAfterGenSecond();
                second=Checker.BeNotNull(sec) ? sec : second;
                Map<String, Object> param = new HashMap<>();
                param.put(Constants.categorys, categoryDtos);
                jobExecute.execute(JobActionNotify.CATEGORY_LIST_PAGE.setMinute(minute).setSecond(second), new JobCategoryListHandler(),param);
            }
        }
    }



    @Override
    public void notifyAllSuccess(Observer observer, ObserverData observerData) {
        log.info("------------SUCCESS--------------");
        log.info(JSON.toJSONString(observer));
        log.info(JSON.toJSONString(observerData));
    }

    @Override
    public void notifyAllSuccess(List<Observer> observers, ObserverData observerData) {
        if(Checker.BeNotEmpty(observers)){
            observers.forEach(observer->{
                notifyAllSuccess(observer,observerData);
            });
        }
    }

    @Override
    public void notifyOnceSuccess(Map<String,Object> resMap) {
          Object userId=resMap.get(SecurityConstants.USER_ID);
          if(Checker.BeNotNull(userId)){
              if(resMap.containsKey(Constants.progress) && Checker.BeNotNull(resMap.get(Constants.progress))){
                  SMT.convertAndSendToUser(userId.toString(),"/queue/msg",resMap);
              }
              System.out.println(resMap.get(Constants.staticFilePath)+"成功!");
              System.out.println(resMap.get(Constants.progress));
          }
    }

    @Override
    public void notifyError(Exception e, Observer observer, ObserverData observerData) {
        boolean action= ObserverAction.valueOf(observerData.getObserverAction().getCode()).equals(ObserverAction.CONTENT_PUBLISH);
        if(true){
            log.error(e.getMessage());
            ApiResult apiResult = ApiResult.result(20006);
            if(Checker.BeNotBlank(observerData.getCreateId())){
                SMT.convertAndSendToUser(observerData.getCreateId(),"/queue/msg",apiResult);
            }
            throw  new CustomException(apiResult);
        }
    }

    @Override
    public void notifyError(Exception e, List<Observer> observers, ObserverData observerData) {
        if(Checker.BeNotEmpty(observers)){
            observers.forEach(observer->{
                notifyError(e,observer,observerData);
            });
        }
    }
}
