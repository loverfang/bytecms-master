package net.bytecms.freemark.directives;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.freemark.corelibs.directive.AbstractTemplateDirective;
import net.bytecms.freemark.corelibs.handler.RenderHandler;
import net.bytecms.service.api.content.ContentService;
import net.bytecms.service.dto.content.ContentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 * 获取公告内容
 */
@Component
public class CmsNoticeDirective extends AbstractTemplateDirective {

    @Autowired
    ContentService contentService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer  rowNum=handler.getInteger(Constants.rowNum);
        String categoryId = handler.getString(Constants.categoryId);
        List<ContentDto> contentDtos=contentService.getTopContent(DirectiveNameEnum.CMS_NOTICE_DIRECTIVE,rowNum,categoryId);
        handler.listToMap(getDirectiveName().getCode(),contentDtos).render();
    }

    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_NOTICE_DIRECTIVE);
    }
}
