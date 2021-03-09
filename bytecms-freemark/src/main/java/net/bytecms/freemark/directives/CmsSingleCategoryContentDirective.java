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
 * 根据分类code 查询分类对应的数据
 */
@Component
public class CmsSingleCategoryContentDirective extends AbstractTemplateDirective {

    @Autowired
    ContentService contentService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String codes=handler.getString(Constants.codes);
        Integer  maxRowNum=handler.getInteger(Constants.rowNum);
        if(Checker.BeNull(maxRowNum)) maxRowNum =6;
        List<ContentDto> contents = contentService.selectContentBySingleCategory(codes,maxRowNum);
        handler.listToMap(getDirectiveName().getCode(),contents).render();
    }

    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_CATEGORY_CONTENG_BYCODE);
    }
}
