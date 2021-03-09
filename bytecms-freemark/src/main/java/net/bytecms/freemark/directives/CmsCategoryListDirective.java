package net.bytecms.freemark.directives;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.utils.Checker;
import net.bytecms.freemark.corelibs.directive.AbstractTemplateDirective;
import net.bytecms.freemark.corelibs.handler.RenderHandler;
import net.bytecms.service.api.category.CmsCategoryService;
import net.bytecms.service.api.content.ContentService;
import net.bytecms.service.dto.content.ContentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Transactional
@Component
public class CmsCategoryListDirective extends AbstractTemplateDirective {

    @Autowired
    ContentService contentService;

    @Autowired
    CmsCategoryService cmsCategoryService;

    @Override
    public void execute(RenderHandler handler) throws Exception {
        Integer pageNo = handler.getInteger(Constants.PAGE_NO);
        Integer pageSize = handler.getInteger(Constants.PAGE_SIZE);
        Integer pageCount = handler.getInteger(Constants.PAGE_COUNT);
        String categoryId = handler.getString(Constants.categoryId);
        if(Checker.BeNotNull(pageNo)){
            PageDto<ContentDto> pageDto =new PageDto<>();
            ContentDto contentDto =new ContentDto();
            contentDto.setCategoryId(categoryId);
            pageDto.setPageSize(pageSize).setPageNo(pageNo).setPageCount(pageCount).setDto(contentDto);
            List<ContentDto> contents=contentService.pageContentForCategoryGen(pageDto);
            handler.listToMap(this.getDirectiveName().getCode(),contents).render();
        }
    }

    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_CATEGORY_LIST_DIRECTIVE);
    }
}
