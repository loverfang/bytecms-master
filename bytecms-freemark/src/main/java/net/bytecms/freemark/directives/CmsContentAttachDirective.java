package net.bytecms.freemark.directives;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.core.utils.Checker;
import net.bytecms.freemark.corelibs.directive.AbstractTemplateDirective;
import net.bytecms.freemark.corelibs.handler.RenderHandler;
import net.bytecms.service.api.content.ContentFileService;
import net.bytecms.service.dto.content.ContentFileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取內容附件
 */
@Component
public class CmsContentAttachDirective extends AbstractTemplateDirective {

    @Autowired
    ContentFileService contentFileService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String contentId = handler.getString(Constants.contentId);
        Boolean hasFiles = handler.getBoolean(Constants.hasFiles);
        List<ContentFileDto> contentFileDtos = null;
        if(Checker.BeNotNull(hasFiles) && !hasFiles){
            contentFileDtos=new ArrayList<>(16);
        }else{
            if(Checker.BeNotBlank(contentId)){
                contentFileDtos= contentFileService.listByField("content_id",contentId);
            }
        }
        handler.listToMap(getDirectiveName().getCode(),contentFileDtos).render();
    }

    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_ATTACH_DIRECTIVE);
    }
}
