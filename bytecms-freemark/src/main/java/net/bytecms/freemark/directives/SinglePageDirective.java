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

/**
 * 查询分类id 查询单个内容
 */
@Component
public class SinglePageDirective extends AbstractTemplateDirective {

    @Autowired
    ContentService contentService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String categoryId = handler.getString(Constants.categoryId);
        if(Checker.BeNotBlank(categoryId)){
            ContentDto contentDto= contentService.selectSinglePageContent(categoryId);
            handler.beanToMap(contentDto).render();
        }
    }

    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_ONE_CONTENT_DIRECTIVE);
    }
}
