package net.bytecms.web.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Luo.z.x
 * @Date 2021/3/11 14:55
 * @Version 1.0
 */
@Controller
@RequestMapping("page")
public class MenuPageController {
    /**
     * 系统菜单页面
     * @return
     */
    @RequestMapping("/menu")
    public String menu(){
        return "system/menu/index";
    }
}
