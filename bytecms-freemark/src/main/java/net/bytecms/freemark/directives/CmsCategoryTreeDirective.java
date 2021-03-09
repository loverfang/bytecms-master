package net.bytecms.freemark.directives;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.core.utils.Tree;
import net.bytecms.freemark.corelibs.directive.AbstractTemplateDirective;
import net.bytecms.freemark.corelibs.handler.RenderHandler;
import net.bytecms.service.api.category.CmsCategoryService;
import net.bytecms.service.dto.category.CmsCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 获取分类栏目首页展示
 */
@Component
public class CmsCategoryTreeDirective extends AbstractTemplateDirective {

    @Autowired
    CmsCategoryService cmsCategoryService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String categoryId = handler.getString(Constants.categoryId);
        Tree<CmsCategoryDto> tree = cmsCategoryService.selectHomePageCategory(categoryId);
        handler.put(getDirectiveName().getCode(),tree.getChildren()).render();
    }

    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_CATEGORY_TREE_DIRECTIVE);
    }
}
