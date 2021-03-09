package net.bytecms.web.proxy;

public interface UserService {
    /**
     * 模拟创建用户并返回用户的id
     * @param userName
     * @param passWord
     * @return
     */
    Integer addUser(String userName,String passWord);
}
