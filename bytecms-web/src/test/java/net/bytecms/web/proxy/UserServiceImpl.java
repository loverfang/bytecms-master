package net.bytecms.web.proxy;

import java.util.Random;

public class UserServiceImpl implements UserService {

    @Override
    public Integer addUser(String userName, String passWord) {
        System.out.println(userName+"|"+passWord);
        System.out.println("创建用户成功!");
        return  new Random().nextInt( 10000 ) + 1;
    }
}
