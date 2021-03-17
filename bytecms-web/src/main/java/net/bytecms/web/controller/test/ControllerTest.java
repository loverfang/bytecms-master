package net.bytecms.web.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Luo.z.x
 * @Date 2021/3/17 17:46
 * @Version 1.0
 */
@RestController
public class ControllerTest {

    @GetMapping("/allow")
    public String allow(){
        return "allow";
    }

    @PostMapping("/authc")
    public String authc(){
        return "authc";
    }

}
