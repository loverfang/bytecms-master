package net.bytecms.freemark.corelibs.handler;

import net.bytecms.core.model.BaseModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * RenderHandler
 * 
 */
public interface RenderHandler {

    /**
     * 渲染
     *
     * @throws IOException
     * @throws Exception
     */
     void render() throws Exception;

    /**
     * 打印
     *
     * @param value
     * @throws IOException
     */
     void print(String value) throws IOException;

    /**
     * 获取Writer
     *
     * @return writer
     * @throws IOException
     */
     Writer getWriter() throws IOException;

    /**
     * @param key
     * @param value
     * @return render handler
     */
     RenderHandler put(String key, Object value);


    /**
     *
     * @param value
     * @return
     */
     RenderHandler putAll(Map value);

    /**
     *
     * @param bean
     * @return
     */
     RenderHandler beanToMap(Object bean);


     RenderHandler beanToMap(String key, Object bean);


     RenderHandler listToMap(String key, List<? extends BaseModel> list);



     RenderHandler putToMap(Map<String,Object> objectMap);



    RenderHandler putToMap(String key,Map<String,Object> objectMap);

    /**
     * @param name
     * @param defaultValue
     * @return string value
     * @throws Exception
     */
     String getString(String name, String defaultValue) throws Exception;

    /**
     * @param name
     *
     * @return string value
     * @throws Exception
     */
     String getString(String name) throws Exception;

    /**
     * @param name
     *
     * @return character value
     * @throws Exception
     */
     Character getCharacter(String name) throws Exception;

    /**
     * @param name
     * @param defaultValue
     * @return int value
     * @throws Exception
     * @throws Exception
     */
     int getInteger(String name, int defaultValue) throws Exception;

    /**
     * @param name
     *
     * @return int value
     * @throws Exception
     */
     Integer getInteger(String name) throws Exception;

    /**
     * @param name
     *
     * @return short value
     * @throws Exception
     */
     Short getShort(String name) throws Exception;

    /**
     * @param name
     *
     * @return long value
     * @throws Exception
     */
     Long getLong(String name) throws Exception;

    /**
     * @param name
     *
     * @return double value
     * @throws Exception
     */
     Double getDouble(String name) throws Exception;

    /**
     * @param name
     *
     * @return int array value
     * @throws Exception
     */
     Integer[] getIntegerArray(String name) throws Exception;

    /**
     * @param name
     *
     * @return long array value
     * @throws Exception
     */
     Long[] getLongArray(String name) throws Exception;
    /**
     * @param name
     *
     * @return long array value
     * @throws Exception
     */
     Short[] getShortArray(String name) throws Exception;

    /**
     * @param name
     *
     * @return string array value
     * @throws Exception
     */
     String[] getStringArray(String name) throws Exception;

    /**
     * @param name
     * @param defaultValue
     * @return bool value
     * @throws Exception
     */
     boolean getBoolean(String name, boolean defaultValue) throws Exception;

    /**
     * @param name
     *
     * @return bool value
     * @throws Exception
     */
     Boolean getBoolean(String name) throws Exception;

    /**
     * @param name
     *
     * @return date value
     * @throws Exception
     */
     Date getDate(String name) throws Exception;

    /**
     * @param name
     * @param defaultValue
     *
     * @return date value
     * @throws Exception
     */
     Date getDate(String name, Date defaultValue) throws Exception;

    /**
     * @return locale
     * @throws Exception
     */
     Locale getLocale() throws Exception;

    /**
     * @return request
     * @throws IOException
     * @throws Exception
     */
     HttpServletRequest getRequest() throws IOException, Exception;

    /**
     * @param name
     * @return attribute
     * @throws IOException
     * @throws Exception
     */
     Object getAttribute(String name) throws IOException, Exception;

    /**
     * set renderd
     */
     void setRenderd();



}