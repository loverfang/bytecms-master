package net.bytecms.web.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description 进入登录页面
 * @Author Luo.z.x
 * @Date 2021/3/8 13:36
 * @Version 1.0
 */
@Controller
@RequestMapping("oauth")
public class LoginPageController{

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

}
