package net.bytecms.web.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Luo.z.x
 * @Date 2021/3/8 18:10
 * @Version 1.0
 */
@Controller
@RequestMapping("page")
public class IndexPageController {
    @RequestMapping("/index")
    public String index(){
        return "index";
    }
}
