package net.bytecms.freemark.tools;

import net.bytecms.core.constants.MethodNameEnum;
import net.bytecms.freemark.corelibs.handler.BaseMethod;
import freemarker.template.TemplateModelException;

import javax.annotation.PostConstruct;
import java.util.List;



public class CmsCategoryPage extends BaseMethod {


    @Override
    public Object exec(List list) throws TemplateModelException {
        return null;
    }

    @PostConstruct
    public void init(){
        this.setMethodName(MethodNameEnum.LBCMS_METHOD_PAGE);
    }
}
