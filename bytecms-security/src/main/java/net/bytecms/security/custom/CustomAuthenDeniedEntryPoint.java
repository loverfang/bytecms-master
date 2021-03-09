package net.bytecms.security.custom;

/**
 * @Author Luo.z.x
 * @Date 2021/3/4 16:14
 * @Version 1.0
 */
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.WebUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenDeniedEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthenDeniedEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.error("-----------");
                WebUtil.write(response, ApiResult.result(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }
}

