package net.bytecms.freemark.directives;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.core.utils.Checker;
import net.bytecms.freemark.corelibs.directive.AbstractTemplateDirective;
import net.bytecms.freemark.corelibs.handler.RenderHandler;
import net.bytecms.service.api.content.CmsContentRelatedService;
import net.bytecms.service.dto.content.ContentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取相關内容
 */
@Component
public class CmsContentRelatedDirective extends AbstractTemplateDirective {

    @Autowired
    CmsContentRelatedService contentRelatedService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String contentId = handler.getString(Constants.contentId);
        Integer  maxRowNum=handler.getInteger(Constants.rowNum);
        Boolean hasRelated = handler.getBoolean(Constants.hasRelated);
        List<ContentDto> contentDtos = new ArrayList<>(16);
        boolean search = Checker.BeNull(hasRelated) || (Checker.BeNotNull(hasRelated) && hasRelated);
        if(search){
            if(Checker.BeNotBlank(contentId)){
                contentDtos= contentRelatedService.getRelatedByContentId(contentId,Checker.BeNotNull(maxRowNum)? maxRowNum:50);
            }
        }
        handler.listToMap(getDirectiveName().getCode(),contentDtos).render();
    }

    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_CONTENT_RELATED_DIRECTIVE);
    }
}
