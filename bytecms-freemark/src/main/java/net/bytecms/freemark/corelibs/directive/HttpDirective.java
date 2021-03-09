package net.bytecms.freemark.corelibs.directive;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * HttpServletDirective 接口指令
 *
 */
public interface HttpDirective {
    /**
     * @param httpMessageConverter
     * @param mediaType
     * @param request
     * @param response
     * @throws IOException
     * @throws Exception
     */
     void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType, HttpServletRequest request,
                  HttpServletResponse response) throws IOException, Exception;
}
