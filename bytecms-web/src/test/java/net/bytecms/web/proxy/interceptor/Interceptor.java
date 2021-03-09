package net.bytecms.web.proxy.interceptor;

import java.lang.reflect.Method;

public interface Interceptor {
    /**
     * 调用前拦截
     * @param proxy 代理对象
     * @param target 真实对象
     * @param method 当前要执行的方法
     * @param args 参数
     * @return
     */
    boolean before(Object proxy, Object target, Method method,Object[] args);

    /**
     * 返回结果通知
     * @param proxy 代理对象
     * @param target 真实对象
     * @param method 当前要执行的方法
     * @param args 参数
     * @return
     */
    void around(Object proxy, Object target, Method method,Object[] args);

    /**
     * 前后都通知
     * @param proxy 代理对象
     * @param target 真实对象
     * @param method 当前要执行的方法
     * @param args 参数
     * @return
     */
    void after(Object proxy, Object target, Method method,Object[] args);

}
