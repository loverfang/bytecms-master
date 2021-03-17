package net.bytecms.freemark.observers;

import net.bytecms.core.config.ByteCmsConfig;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.freemark.components.TemplateComponent;
import net.bytecms.freemark.corelibs.notify.AbstractNotify;
import net.bytecms.freemark.corelibs.observer.AdapterObserver;
import net.bytecms.freemark.corelibs.observer.ObserverData;
import net.bytecms.freemark.corelibs.observer.data.HomePageObserverData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class HomePageObserver extends AdapterObserver {

    @Autowired
    TemplateComponent templateComponent;


    @Autowired
    ByteCmsConfig byteCmsConfig;


    @Override
    public void update(ObserverData data) {
        // 组装数据 创建静态页
        if(Checker.BeNotNull(data)){
            if(data instanceof HomePageObserverData){
                HomePageObserverData homePageData = (HomePageObserverData) data;
                String homePageTemp = Constants.DEFAULT_HOME_PAGE;
                String homePageStatic = byteCmsConfig.getSiteStaticFileRootPath()+ File.separator+Constants.DEFAULT_HOME_PAGE;
                templateComponent.createIndexStaticFile(homePageTemp,homePageStatic,homePageData.getMapData());
            }
        }
    }

    @Override
    public void update(ObserverData data, AbstractNotify notifyRes) {
        // 组装数据 创建静态页
        if(Checker.BeNotNull(data)){
            if(data instanceof HomePageObserverData){
                HomePageObserverData homePageData = (HomePageObserverData) data;
                String homePageTemp = Constants.DEFAULT_HOME_PAGE;
                String homePageStatic = byteCmsConfig.getSiteStaticFileRootPath()+ File.separator+Constants.DEFAULT_HOME_PAGE;
                ApiResult apiResult=templateComponent.createIndexStaticFile(homePageTemp,homePageStatic,homePageData.getMapData());
                apiResult.put(Constants.progress,10);
                notifyRes(apiResult,notifyRes,data.getCreateId());
            }
        }
    }

    private void notifyRes(ApiResult apiResult,AbstractNotify notifyRes,String createId){
        if(apiResult.ckSuccess()){
            apiResult.put(Constants.progress,100);
            if(Checker.BeNotBlank(createId)){
                apiResult.put(SecurityConstants.USER_ID,createId);
            }
            notifyRes.notifyOnceSuccess(apiResult);
        }
    }
}
