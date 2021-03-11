package net.bytecms.web.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Luo.z.x
 * @Date 2021/3/10 17:58
 * @Version 1.0
 */
@Controller
@RequestMapping("page")
public class DashboardPageController {

    @RequestMapping("/dashboard")
    public String dashboard(){
        return "/system/dashboard/analysis";
    }
}
