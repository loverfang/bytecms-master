package net.bytecms.freemark.directives;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.core.utils.Checker;
import net.bytecms.freemark.corelibs.directive.AbstractTemplateDirective;
import net.bytecms.freemark.corelibs.handler.RenderHandler;
import net.bytecms.service.api.category.CmsCategoryService;
import net.bytecms.service.dto.navigation.Navigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
public class CmsNavigation extends AbstractTemplateDirective {

    @Autowired
    CmsCategoryService categoryService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String contentId = handler.getString(Constants.contentId);
        if (Checker.BeNotBlank(contentId)) {
            List<Navigation> navigations = categoryService.getNavigation(contentId);
            handler.listToMap(getDirectiveName().getCode(),navigations).render();
        }
    }

    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_NAVIGATION);
    }
}
