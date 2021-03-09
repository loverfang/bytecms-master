package net.bytecms.freemark.directives;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.core.utils.Checker;
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
 * 查询分类id 查询单个内容
 */
@Component
public class CmsTopTagDirective extends AbstractTemplateDirective {

    @Autowired
    ContentService contentService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String topTag = handler.getString(Constants.topTag);
        String categoryId = handler.getString(Constants.categoryId);
        Integer rowNum = handler.getInteger(Constants.rowNum);
        if (Checker.BeNotBlank(topTag)) {
            rowNum=Checker.BeNotNull(rowNum)?rowNum:10;
            List<ContentDto> contentDtos= contentService.getContentsByTopTag(topTag,rowNum,categoryId);
            handler.listToMap(getDirectiveName().getCode(),contentDtos).render();
        }
    }

    @PostConstruct
    public void initName() {
        this.setDirectiveName(DirectiveNameEnum.CMS_TOP_TAG_DIRECTIVE);
    }
}
