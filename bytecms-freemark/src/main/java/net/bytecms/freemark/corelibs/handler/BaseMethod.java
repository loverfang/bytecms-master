package net.bytecms.freemark.corelibs.handler;
import net.bytecms.core.constants.MethodNameEnum;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.TemplateModelUtils;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * BaseMethod FreeMarker自定义方法基类
 *
 */
@Data
public abstract class BaseMethod implements TemplateMethodModelEx {
    protected final Log log = LogFactory.getLog(getClass());

    protected MethodNameEnum methodName;

    private static TemplateModel getModel(int index, List<TemplateModel> arguments) {
        if (Checker.BeNotEmpty(arguments) && index < arguments.size()) {
            return arguments.get(index);
        }
        return null;
    }

    /**
     * @param index
     * @param arguments
     * @return map value
     * @throws TemplateModelException
     */
    public static TemplateHashModelEx getMap(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converMap(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return string value
     * @throws TemplateModelException
     */
    public static String getString(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converString(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return int value
     * @throws TemplateModelException
     */
    public static Integer getInteger(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converInteger(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return short value
     * @throws TemplateModelException
     */
    public static Short getShort(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converShort(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return long value
     * @throws TemplateModelException
     */
    public static Long getLong(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converLong(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return double value
     * @throws TemplateModelException
     */
    public static Double getDouble(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converDouble(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return string array value
     * @throws TemplateModelException
     */
    public static String[] getStringArray(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converStringArray(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return int array value
     * @throws TemplateModelException
     */
    public static Integer[] getIntegerArray(int index, List<TemplateModel> arguments) throws TemplateModelException {
        String[] arr = getStringArray(index, arguments);
        if (Checker.BeNotEmpty(arr)) {
            Set<Integer> set = new TreeSet<>();
            for (String s : arr) {
                try {
                    set.add(Integer.valueOf(s));
                } catch (NumberFormatException e) {
                }
            }
            return set.toArray(new Integer[set.size()]);
        }
        return null;
    }

    /**
     * @param index
     * @param arguments
     * @return long array value
     * @throws TemplateModelException
     */
    public static Long[] getLongArray(int index, List<TemplateModel> arguments) throws TemplateModelException {
        String[] arr = getStringArray(index, arguments);
        if (Checker.BeNotEmpty(arr)) {
            Set<Long> set = new TreeSet<>();
            for (String s : arr) {
                try {
                    set.add(Long.valueOf(s));
                } catch (NumberFormatException e) {
                }
            }
            return set.toArray(new Long[set.size()]);
        }
        return null;
    }

    /**
     * @param index
     * @param arguments
     * @return bool value
     * @throws TemplateModelException
     */
    public static Boolean getBoolean(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converBoolean(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return date
     * @throws TemplateModelException
     * @throws ParseException
     */
    public static Date getDate(int index, List<TemplateModel> arguments) throws TemplateModelException, ParseException {
        return TemplateModelUtils.converDate(getModel(index, arguments));
    }

    /**
     * @return whether to enable http
     */
    public boolean httpEnabled() {
        return true;
    }


}
