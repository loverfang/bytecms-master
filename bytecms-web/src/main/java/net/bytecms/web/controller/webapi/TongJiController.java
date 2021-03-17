package net.bytecms.web.controller.webapi;

import net.bytecms.service.api.content.ContentService;
import net.bytecms.core.config.ByteCmsConfig;
import net.bytecms.core.utils.HttpContextUtils;
import net.bytecms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 内容 前端控制器
 * </p>
 *
 * @author LG
 * @since 2019-10-30
 */
@Validated
@RestController
@RequestMapping("/api/tongji")
public class TongJiController extends BaseController<ContentService> {

    @Autowired
    ByteCmsConfig byteCmsConfig;

    @GetMapping("goToBaiDuTj")
    public void goToBaiDuTj(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean mobile=HttpContextUtils.ckIsMobile();
        String url = mobile? byteCmsConfig.getBaiDuTongjiUrlM(): byteCmsConfig.getBaiDuTongJiUrl();
        response.sendRedirect(url);
    }

}
