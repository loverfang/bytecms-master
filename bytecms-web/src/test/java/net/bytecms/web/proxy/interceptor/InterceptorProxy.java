package net.bytecms.web.proxy.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InterceptorProxy implements InvocationHandler {
    private Object target;
    private String interceptorClassName;
    public InterceptorProxy(Object target,String interceptorClassName){
        this.target=target;
        this.interceptorClassName = interceptorClassName;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(this.interceptorClassName==null){
            return method.invoke(target,args);
        }
        Object object=null;
        Interceptor interceptor = (Interceptor) Class.forName(interceptorClassName).newInstance();
        if(interceptor.before(proxy,target,method,args)){
            object= method.invoke(target,args);
        }else{
            interceptor.around(proxy,target,method,args);
        }
        interceptor.after(proxy,target,method,args);
        return object;
    }

    /**
     * 产生代理对象
     * @param target 真实对象
     * @param interceptorClassName 拦截器限定名
     * @return
     */
    public static Object genInterceptorProxy(Object target,String interceptorClassName){
         return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),
         new InterceptorProxy(target,interceptorClassName) );
    }
}
