package net.bytecms.freemark.corelibs.directive;

import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.freemark.corelibs.handler.TemplateDirectiveHandler;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import lombok.Data;

import java.io.IOException;
import java.util.Map;

@Data
public abstract class BaseTemplateDirective implements TemplateDirectiveModel, Directive,HttpDirective{

    private DirectiveNameEnum directiveName;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment environment,  Map parameters, TemplateModel[] loopVars,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        try {
            execute(new TemplateDirectiveHandler(parameters, loopVars, environment, templateDirectiveBody));
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateException(e, environment);
        }
    }

}
