package net.bytecms.web.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Luo.z.x
 * @Date 2021/3/11 10:57
 * @Version 1.0
 */
@Controller
@RequestMapping("page")
public class SysUserPageController {

    /**
     * 系统用户首页
     * @return
     */
    @RequestMapping("/sysUser")
    public String sysUser(){
        return "system/sysuser/index";
    }

}
