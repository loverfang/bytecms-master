package net.bytecms.web.proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
public class UserProxyExample implements InvocationHandler {
    /**
     * 真实对象
     */
    private  Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(args.length==2){
            if(args[0].toString().length()>10 || args[0].toString().length()<5){ //校验用户名称是否符合规则
                throw new Exception("用户名不合法");
            }
            if(args[1].toString().length()>10 || args[1].toString().length()<5){ //校验用户名称是否符合规则
                throw new Exception("密码不合法");
            }
            return method.invoke(target,args);

        }
        return null;
    }


    /**
     * 产生代理对象 newProxyInstance 有三个参数
     * 1：类加载器,此处采用的真实对象的类加载器
     * 2：把生成的代理对象挂在到哪个接口下，真实对象为 UserServiceImpl 的接口为 UserService
     * 3：定义实现代理逻辑的类（该类必须实现InvocationHandler） 我们在此处采用 当前类
     * @param target
     * @return
     */
    public Object genProxyObject(Object target){
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }
}
