package net.bytecms.freemark.observers;

import net.bytecms.core.config.ThinkCmsConfig;
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
import java.util.Map;

/**
 * 批量生成指定页面
 */
@Component
public class DynamicPageObserver extends AdapterObserver {

    @Autowired
    TemplateComponent templateComponent;


    @Autowired
    ThinkCmsConfig thinkCmsConfig;


    @Override
    public void update(ObserverData data, AbstractNotify notifyRes) {
        // 组装数据 创建静态页
        if(Checker.BeNotNull(data)){
            if(data instanceof HomePageObserverData){
                HomePageObserverData dynamicPageData = (HomePageObserverData) data;
                Map<String,Object> mapData= dynamicPageData.getMapData();
                String userId = dynamicPageData.getCreateId();
                if(!mapData.isEmpty()){
                    for (Map.Entry<String, Object> m : mapData.entrySet()) {
                        if(Checker.BeNotNull(m)){
                            String tempPath = m.getKey()+ Constants.DEFAULT_HTML_SUFFIX;
                            String staticPath = thinkCmsConfig.getSiteStaticFileRootPath()+ File.separator+m.getValue()+Constants.DEFAULT_HTML_SUFFIX;
                            ApiResult apiResult=templateComponent.createIndexStaticFile(tempPath,staticPath,mapData);
                            if(apiResult.ckSuccess()){
                                apiResult.put(Constants.progress,100);
                                apiResult.put(SecurityConstants.USER_ID,userId);
                                notifyRes.notifyOnceSuccess(apiResult);
                            }
                        }
                    }
                }
            }
        }
    }
}
