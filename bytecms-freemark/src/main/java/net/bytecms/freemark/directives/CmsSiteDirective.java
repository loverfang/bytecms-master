package net.bytecms.freemark.directives;

import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.freemark.corelibs.directive.AbstractTemplateDirective;
import net.bytecms.freemark.corelibs.handler.RenderHandler;
import net.bytecms.service.api.site.CmsSiteService;
import net.bytecms.service.dto.site.CmsSiteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 获取内容标签
 */
@Component
public class CmsSiteDirective extends AbstractTemplateDirective {

    @Autowired
    CmsSiteService cmsSiteService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        CmsSiteDto cmsSiteDto= cmsSiteService.getSite();
        handler.beanToMap(cmsSiteDto).render();
    }

    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_SITE_DIRECTIVE);
    }
}
