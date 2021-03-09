package net.bytecms.freemark.corelibs.handler;

import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.TemplateModelUtils;
import freemarker.core.Environment;
import freemarker.core.Environment.Namespace;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * TemplateDirectiveHandler
 *
 */
public class TemplateDirectiveHandler extends BaseHandler {
    private Map<String, TemplateModel> parameters;
    private TemplateModel[] loopVars;
    private TemplateDirectiveBody templateDirectiveBody;
    private Environment environment;

    /**
     * @param parameters
     * @param loopVars
     * @param environment
     * @param templateDirectiveBody
     * @throws Exception
     */
    public TemplateDirectiveHandler(Map<String, TemplateModel> parameters, TemplateModel[] loopVars, Environment environment,
                                    TemplateDirectiveBody templateDirectiveBody) throws Exception {
        this.parameters = parameters;
        this.loopVars = loopVars;
        this.templateDirectiveBody = templateDirectiveBody;
        this.environment = environment;
        regristerParameters();
    }

    @Override
    public RenderHandler put(String key, Object value) {
        return super.put(key, value);
    }

    @Override
    public RenderHandler putAll(Map value) {
        return super.putAll( value);
    }

//    如有特殊定制可重写该方法否则执行默认指令数据分包装器
//    @Override
//    public RenderHandler beanToMap(Object obj) {
//        return this;
//    }

    @Override
    public void render() throws TemplateException, IOException {
        if (!renderd) {
            Map<String, TemplateModel> reduceMap = reduce();
            if (null != templateDirectiveBody) {
                templateDirectiveBody.render(environment.getOut());
            }
            reduce(reduceMap);
            renderd = true;
        }
    }

    @Override
    public void print(String value) throws IOException {
        environment.getOut().write(value);
    }

    @Override
    public Writer getWriter() {
        return environment.getOut();
    }

    private Map<String, TemplateModel> reduce() throws TemplateModelException {
        Map<String, TemplateModel> reduceMap = new LinkedHashMap<>();
        ObjectWrapper objectWrapper = environment.getObjectWrapper();
        Namespace namespace = environment.getCurrentNamespace();
        Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            Entry<String, Object> entry = iterator.next();
            if (i < loopVars.length) {
                loopVars[i] = objectWrapper.wrap(entry.getValue());
            } else {
                String key = entry.getKey();
                reduceMap.put(key, namespace.get(key));
                namespace.put(key, objectWrapper.wrap(entry.getValue()));
            }
        }
        return reduceMap;
    }

    private void reduce(Map<String, TemplateModel> reduceMap) {
        if (Checker.BeNotEmpty(reduceMap)) {
            Namespace namespace = environment.getCurrentNamespace();
            namespace.putAll(reduceMap);
        }
    }

    /**
     * @param name
     * @return map value
     * @throws TemplateModelException
     */
    public TemplateHashModelEx getMap(String name) throws TemplateModelException {
        return TemplateModelUtils.converMap(parameters.get(name));
    }

    @Override
    protected String getStringWithoutRegister(String name) throws TemplateModelException {
        return TemplateModelUtils.converString(parameters.get(name));
    }

    @Override
    public Integer getIntegerWithoutRegister(String name) throws TemplateModelException {
        return TemplateModelUtils.converInteger(parameters.get(name));
    }

    @Override
    public Short getShort(String name) throws TemplateModelException {
        regristerParameter(PARAMETER_TYPE_SHORT, name);
        return TemplateModelUtils.converShort(parameters.get(name));
    }

    @Override
    public Long getLong(String name) throws TemplateModelException {
        regristerParameter(PARAMETER_TYPE_LONG, name);
        return TemplateModelUtils.converLong(parameters.get(name));
    }

    @Override
    public Double getDouble(String name) throws TemplateModelException {
        regristerParameter(PARAMETER_TYPE_DOUBLE, name);
        return TemplateModelUtils.converDouble(parameters.get(name));
    }

    @Override
    protected String[] getStringArrayWithoutRegister(String name) throws TemplateModelException {
        return TemplateModelUtils.converStringArray(parameters.get(name));
    }

    @Override
    protected Boolean getBooleanWithoutRegister(String name) throws TemplateModelException {
        return TemplateModelUtils.converBoolean(parameters.get(name));
    }

    @Override
    public Date getDateWithoutRegister(String name) throws TemplateModelException, ParseException {
        return TemplateModelUtils.converDate(parameters.get(name));
    }

    @Override
    public Locale getLocale() throws Exception {
        return environment.getLocale();
    }

    @Override
    public HttpServletRequest getRequest() throws IOException, Exception {
        HttpRequestHashModel httpRequestHashModel = (HttpRequestHashModel) environment.getGlobalVariable("Request");
        if (null != httpRequestHashModel) {
            return httpRequestHashModel.getRequest();
        }
        return null;
    }

    @Override
    public Object getAttribute(String name) throws IOException, Exception {
        TemplateModel model = environment.getGlobalVariable(name);
        if (null != model) {
            return TemplateModelUtils.converBean(model);
        }
        return null;
    }
}