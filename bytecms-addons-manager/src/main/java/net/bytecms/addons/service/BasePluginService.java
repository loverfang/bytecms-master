package net.bytecms.addons.service;

import net.bytecms.addons.handler.AddonHandler;
import net.bytecms.addons.interceptor.Interceptor;
import net.bytecms.core.annotation.AddonsMapping;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SpringContextHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class BasePluginService {

    public  <T> T getService(Class<T> clz, Interceptor interceptor) throws Exception {
        Object object=clz.newInstance();
        return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(),object.getClass().getInterfaces(),new AddonHandler(object,interceptor));
    }

    public  <T> T getBean(Class<T> clz)  {
        return (T) SpringContextHolder.getBean(clz);
    }

    protected Object executeMethod(Object service, String api, String requestMethod, Map<String, Object> params) throws InvocationTargetException, IllegalAccessException {
        Object result=null;
        if(Checker.BeNotNull(service)){
             Class[] interfaces=service.getClass().getInterfaces();
             if(interfaces.length>0){
                 Method[] methods= interfaces[0].getMethods();
                 if(methods.length>0){
                     for(Method method:methods){
                         AddonsMapping addonsMapping=method.getAnnotation(AddonsMapping.class);
                         if(Checker.BeNotNull(addonsMapping)){
                             String apiUrl=Checker.BeNotBlank(addonsMapping.name())?addonsMapping.name():method.getName();
                             if(api.equals(apiUrl)){
                                 result= method.invoke(service,params);
                             }
                         }
                     }
                 }
             }

         }
        return result;
    }

}
