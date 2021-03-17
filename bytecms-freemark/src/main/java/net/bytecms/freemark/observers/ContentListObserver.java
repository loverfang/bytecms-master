package net.bytecms.freemark.observers;

import net.bytecms.core.config.ByteCmsConfig;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.model.PageKeyWord;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.freemark.components.TemplateComponent;
import net.bytecms.freemark.corelibs.notify.AbstractNotify;
import net.bytecms.freemark.corelibs.observer.AdapterObserver;
import net.bytecms.freemark.corelibs.observer.ObserverAction;
import net.bytecms.freemark.corelibs.observer.ObserverData;
import net.bytecms.freemark.corelibs.observer.data.ContentPublishObserverData;
import net.bytecms.freemark.tools.CategoryPageHelp;
import net.bytecms.service.api.category.CmsCategoryAttributeService;
import net.bytecms.service.api.content.ContentService;
import net.bytecms.service.dto.category.CmsCategoryDto;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@Data
@Component
public class ContentListObserver extends AdapterObserver {

    @Autowired
    TemplateComponent templateComponent;

    @Autowired
    ContentService contentService;

    @Autowired
    ByteCmsConfig byteCmsConfig;

    @Autowired
    CmsCategoryAttributeService cmsCategoryAttributeService;

    @Override
    public void update(ObserverData data, AbstractNotify notifyRes) {
        if (Checker.BeNotNull(data)) {
            ObserverAction code = ObserverAction.valueOf(data.getObserverAction().getCode());
            if (code.equals(ObserverAction.CATEGORY_CHANGE) || code.equals(ObserverAction.CONTENT_PUBLISH)) {
                if (data instanceof ContentPublishObserverData) {
                    genCatCategory(((ContentPublishObserverData) data).getCmsCategoryDtos(),notifyRes,data.getCreateId());
                }
            }
        }
    }

    @Override
    public void update(ObserverData data) {
        update(data,null);
    }


    private void genCatCategory(List<CmsCategoryDto> categoryDtos, AbstractNotify notifyRes, String createId) {
        if (Checker.BeNotEmpty(categoryDtos)) {
            Map<String, PageKeyWord> mapKeyWord=cmsCategoryAttributeService.listPageKeyWordByCategorys(categoryDtos);
            for (CmsCategoryDto cmsCategoryDto : categoryDtos) {
                Integer pageCount=contentService.calculatePageCountByCategoryId(cmsCategoryDto.getId(),cmsCategoryDto.getPageSize());
                genStaticFile(cmsCategoryDto,pageCount,notifyRes,createId,mapKeyWord.get(cmsCategoryDto.getId()));
            }
        }
    }

    private void genStaticFile(CmsCategoryDto categoryDto, Integer pageCount,AbstractNotify notifyRes,String createId, PageKeyWord pageKeyWord){
        if(pageCount>0){
            int progress;
            DecimalFormat df=new DecimalFormat("0.00");//设置保留位数
            for(int i=1;i<=pageCount;i++){
                boolean autoGen = categoryDto.getAutoGenStatic() && i > categoryDto.getMaxStaticPage();
                if(autoGen){
                    continue;
                }
                String fileName= i==1 ? "index": "index_"+i;
                String staticFilePath = byteCmsConfig.getSiteStaticFileRootPath()+ File.separator+categoryDto.getCode()+
                File.separator+fileName+Constants.DEFAULT_HTML_SUFFIX;
                CategoryPageHelp pageHelp = new CategoryPageHelp(categoryDto,i,pageCount);
                if(Checker.BeNotNull(pageKeyWord)){
                    pageHelp.put(Constants.title, Checker.BeNotBlank(pageKeyWord.getTitle()) ? pageKeyWord.getTitle() : "");
                    pageHelp.put(Constants.keywords, Checker.BeNotBlank(pageKeyWord.getKeywords()) ? pageKeyWord.getKeywords() : "");
                    pageHelp.put(Constants.description, Checker.BeNotBlank(pageKeyWord.getDescription()) ? pageKeyWord.getDescription() : "");
                }
                ApiResult apiResult=templateComponent.createCategoryStaticFile(categoryDto.getTemplatePath(),staticFilePath,pageHelp);
                if(apiResult.ckSuccess() && Checker.BeNotNull(notifyRes)){
                    String progressNum=df.format((float)i/pageCount);
                    progress= (int) (Double.valueOf(progressNum)*100) ;
                    apiResult.put(Constants.progress,progress);
                    if(Checker.BeNotBlank(createId)){
                        apiResult.put(SecurityConstants.USER_ID,createId);
                    }
                    notifyRes.notifyOnceSuccess(apiResult);
                }
            }
        }
    }

}
