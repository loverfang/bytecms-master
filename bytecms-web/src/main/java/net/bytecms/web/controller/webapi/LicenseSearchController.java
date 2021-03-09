package net.bytecms.web.controller.webapi;

import net.bytecms.core.utils.ApiResult;
import net.bytecms.system.api.system.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api/license")
public class LicenseSearchController {

    @Autowired
    private LicenseService licenseService;

    @GetMapping("search")
    public ApiResult search(){
        return licenseService.search();
    }
}
