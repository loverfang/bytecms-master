package net.bytecms.freemark.directives;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.freemark.corelibs.directive.AbstractTemplateDirective;
import net.bytecms.freemark.corelibs.handler.RenderHandler;
import net.bytecms.service.api.fragment.FragmentService;
import net.bytecms.service.dto.fragment.FragmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
public class CmsFragmentDirective extends AbstractTemplateDirective {

    @Autowired
    FragmentService fragmentService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String code=handler.getString(Constants.code);
        List<FragmentDto> fragmentDtos=fragmentService.getFragmentDataByCode(code);
        handler.listToMap(this.getDirectiveName().getCode(),fragmentDtos).render();
    }

    @PostConstruct
    public void initName(){
        this.setDirectiveName(DirectiveNameEnum.CMS_FRAGMENT_DATA_DIRECTIVE);
    }
}
