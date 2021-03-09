package net.bytecms.web.proxy;

import org.junit.Test;

public class Demo {

    @Test
    public void tes(){
        UserProxyExample userProxyExample=new UserProxyExample();
        UserService userServiceProxy=(UserService)userProxyExample.genProxyObject(new UserServiceImpl());
        userServiceProxy.addUser("admin","111111");
    }

}
