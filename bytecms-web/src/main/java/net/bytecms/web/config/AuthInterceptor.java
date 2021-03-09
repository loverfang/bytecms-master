package net.bytecms.web.config;

import net.bytecms.core.handler.CustomException;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.BaseContextKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String reqM= request.getMethod();
        if("manager".equals(BaseContextKit.getUserName())){
            if("get".equalsIgnoreCase(reqM)){
                return true;
            }
            else{
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                String methodName = method.getName();
                log.info("====拦截到了方法：{}，在该方法执行之前执行====", methodName);
                // 返回 true 才会继续执行，返回 false 则取消当前请求
                if(isAllow(methodName)){
                    return true;
                }
                throw new CustomException(ApiResult.result(20022));
            }

        }else{
            return true;
        }
    }

    private boolean isAllow(String methodName){
        boolean flag = false;
        List<String> allowMethods=allowMethods();
        for(String allowMethod:allowMethods){
            boolean ck = allowMethod.equalsIgnoreCase(methodName)||allowMethod.contains(methodName)|| methodName.startsWith(allowMethod);
            if(ck){
                flag = true;
                break;
            }
        }
        return flag;
    }

    private List<String> allowMethods(){
        List<String> allows=new ArrayList<>();
        allows.add("page");
        allows.add("select");
        allows.add("get");
        allows.add("list");
        allows.add("keepUploadFile");
        return allows;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //log.info("执行完方法之后进执行(Controller方法调用之后)，但是此时还没进行视图渲染");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //log.info("整个请求都处理完咯，DispatcherServlet也渲染了对应的视图咯，此时我可以做一些清理的工作了");
    }

}
