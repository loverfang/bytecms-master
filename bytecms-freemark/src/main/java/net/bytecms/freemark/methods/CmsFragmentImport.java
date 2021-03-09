package net.bytecms.freemark.methods;

import net.bytecms.core.constants.MethodNameEnum;
import net.bytecms.core.utils.Checker;
import net.bytecms.freemark.corelibs.handler.BaseMethod;
import net.bytecms.service.api.fragment.FragmentFileModelService;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class CmsFragmentImport extends BaseMethod {

    @Autowired
    FragmentFileModelService fragmentFileModelService;

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        String code = getString(0, arguments);
        if (Checker.BeNotBlank(code)) {
            String fragmentPath = fragmentFileModelService.getFragmentFilePathByCode(code);
            if (Checker.BeNotBlank(fragmentPath)) {
                return fragmentPath;
            }
        }
        return null;
    }
    @PostConstruct
    public void init(){
        this.setMethodName(MethodNameEnum.LBCMS_FRAGMENT_IMPORT);
    }
}
