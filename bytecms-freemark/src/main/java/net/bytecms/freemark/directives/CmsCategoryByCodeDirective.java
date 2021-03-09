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
import java.util.List;

/**
 * 根据code 获取分类栏目
 */
@Component
public class CmsCategoryByCodeDirective extends AbstractTemplateDirective {

    @Autowired
    CmsCategoryService cmsCategoryService;


    @Override
    public void execute(RenderHandler handler) throws  Exception {
        String[] codes= handler.getStringArray(Constants.codes);
        List<CmsCategoryDto> categorys=cmsCategoryService.selectCategoryByCodes(codes);
        handler.listToMap(getDirectiveName().getCode(),categorys).render();
    }
    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_CATEGORY_BY_CODE_DIRECTIVE);
    }
}
