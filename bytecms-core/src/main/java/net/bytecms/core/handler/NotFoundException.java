//package com.org.wechat.core.handler;
//
//import com.org.wechat.core.utils.ApiResult;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
//@RestController
//public class NotFoundException implements ErrorController {
//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }
//
//    @RequestMapping(value = {"/error"})
//    public Object error(HttpServletRequest request) {
//        return ApiResult.result(404);
//    }
//}
