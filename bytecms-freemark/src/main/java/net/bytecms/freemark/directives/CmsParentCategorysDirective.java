package net.bytecms.freemark.directives;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.freemark.corelibs.directive.AbstractTemplateDirective;
import net.bytecms.freemark.corelibs.handler.RenderHandler;
import net.bytecms.service.api.category.CmsCategoryService;
import net.bytecms.service.dto.category.CmsCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 * 根据zi分类查询父分类指令
 */
@Component
public class CmsParentCategorysDirective extends AbstractTemplateDirective {

    @Autowired
    CmsCategoryService cmsCategoryService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String id = handler.getString(Constants.categoryId);
        List<CmsCategoryDto> pcategorys = cmsCategoryService.selectParentCategorys(id);
        handler.listToMap(getDirectiveName().getCode(),pcategorys).render();
    }

    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_PARENT_CATEGORY_DIRECTIVE);
    }
}
