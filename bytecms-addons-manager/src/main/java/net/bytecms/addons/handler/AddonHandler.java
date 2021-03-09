package net.bytecms.addons.handler;

import net.bytecms.addons.interceptor.Interceptor;
import net.bytecms.core.annotation.AddonInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AddonHandler implements InvocationHandler {

    private Object target;

    private Interceptor interceptor;

    public AddonHandler(Object target, Interceptor interceptor) throws Exception {
        this.target=target;
        this.interceptor=interceptor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object object=null;
       if(interceptor!=null){
           AddonInterceptor addonInterceptor= method.getAnnotation(AddonInterceptor.class);
           if(addonInterceptor!=null){
               if(interceptor.before(proxy,target,method,args)){
                   object=method.invoke(target,args);
               }else{
                   interceptor.around(proxy,target,method,args);
               }
               interceptor.after(proxy,target,method,args);
           }
           return object;
       }else{
           return method.invoke(target,args);
       }
    }
}
