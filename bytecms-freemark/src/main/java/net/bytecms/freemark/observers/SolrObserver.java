package net.bytecms.freemark.observers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.bytecms.core.api.BaseSolrService;
import net.bytecms.core.config.ThinkCmsConfig;
import net.bytecms.core.constants.SolrCoreEnum;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.freemark.components.TemplateComponent;
import net.bytecms.freemark.corelibs.notify.AbstractNotify;
import net.bytecms.freemark.corelibs.observer.AdapterObserver;
import net.bytecms.freemark.corelibs.observer.ObserverAction;
import net.bytecms.freemark.corelibs.observer.ObserverData;
import net.bytecms.freemark.corelibs.observer.data.SolrObserverData;
import net.bytecms.service.api.content.ContentService;
import net.bytecms.service.api.resource.SysResourceService;
import net.bytecms.service.dto.content.ContentDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

@Data
@Component
@Slf4j
public class SolrObserver extends AdapterObserver {

    @Autowired
    TemplateComponent templateComponent;

    @Autowired
    ContentService contentService;

    @Autowired
    SysResourceService resourceService;

    @Autowired
    ThinkCmsConfig thinkCmsConfig;

    @Autowired
    BaseSolrService baseSolrService;

    int step=0;

    @Override
    public void update(ObserverData data, AbstractNotify notifyRes) {
        // 组装数据 创建静态页
        if (Checker.BeNotNull(data)) {
            if (ObserverAction.valueOf(data.getObserverAction().getCode()).equals(ObserverAction.SOLR_CHANGE)) {
                if (data instanceof SolrObserverData) {
                    SolrObserverData obsData = (SolrObserverData) data;
                     try {
                         try {
                             baseSolrService.deleteAll(SolrCoreEnum.DEFAULT_CORE);
                         } catch (IOException|SolrServerException e) {
                             log.error(e.getMessage());
                             throw new CustomException(ApiResult.result(20020));
                         }
                         syncSolrData(obsData,notifyRes);
                     }finally {
                         this.step=0;
                     }
                }
            }
        }
    }

    private void syncSolrData(SolrObserverData obsData,AbstractNotify notifyRes) {
        pageContent(obsData.getCreateId(),1,notifyRes);
    }

    private ApiResult pageContent(String createId,Integer pageNo,AbstractNotify notifyRes){
        ApiResult apiResult =ApiResult.result();
        IPage<ContentDto> pages = new Page<>(pageNo, 100);
        IPage<ContentDto> result = contentService.pageAllContentForToSolr(pages,Arrays.asList(new String[]{"1"}));
        if(result.getCurrent() <= result.getPages() && Checker.BeNotEmpty(result.getRecords())){
            saveSolr(result.getRecords(),createId,notifyRes,result);
            pageNo+=1;
            apiResult=pageContent(createId, pageNo,notifyRes);
        }
        return apiResult;
    }

    private void notifyFront(String createId,AbstractNotify notifyRes,IPage<ContentDto> result,String title){
        this.step+=1;
        DecimalFormat df=new DecimalFormat("0.00");//设置保留位数
        ApiResult apiResult =ApiResult.result();
        String progressNum=df.format((float)step/result.getTotal());
        apiResult.put("progress",(int) (Double.valueOf(progressNum)*100));
        apiResult.put("staticFilePath",title);
        apiResult.put(SecurityConstants.USER_ID,createId);
        notifyRes.notifyOnceSuccess(apiResult);
    }

    private void saveSolr(List<ContentDto> contents,String createId,AbstractNotify notifyRes,IPage<ContentDto> result){
        if(Checker.BeNotEmpty(contents)){
            contents.forEach(contentDto -> {
                try {
                    baseSolrService.pushToSolr(SolrCoreEnum.DEFAULT_CORE,contentDto);
                    notifyFront(createId,notifyRes, result,contentDto.getTitle());
                } catch (Exception e) {
                    log.error("SOLR 同步失败:"+e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }
}
