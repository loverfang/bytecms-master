package net.bytecms.freemark.directives;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.core.utils.Checker;
import net.bytecms.freemark.corelibs.directive.AbstractTemplateDirective;
import net.bytecms.freemark.corelibs.handler.RenderHandler;
import net.bytecms.service.api.content.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * 根据分类code 查询分类对应的数据
 */
@Component
public class CmsCategoryContentDirective extends AbstractTemplateDirective {

    @Autowired
    ContentService contentService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String[] codes=handler.getStringArray(Constants.codes);
        Integer  maxRowNum=handler.getInteger(Constants.rowNum);
        if(Checker.BeNull(maxRowNum)) maxRowNum =6;
        Map<String,Object> contents = contentService.selectContentByCategory(codes,maxRowNum);
        handler.putToMap(getDirectiveName().getCode(),contents).render();
    }

    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_CATEGORY_CONTENT_FOR_PAGE_HOME_DIRECTIVE);
    }
}
