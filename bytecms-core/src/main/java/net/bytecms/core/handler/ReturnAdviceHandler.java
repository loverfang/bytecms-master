package net.bytecms.core.handler;

import com.google.common.collect.Lists;
import net.bytecms.core.annotation.NotDecorate;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;


@RestControllerAdvice
public class ReturnAdviceHandler implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		boolean bePrivate = Modifier.PRIVATE == returnType.getMethod().getModifiers();
		if (bePrivate) {
			return false;
		}
		NotDecorate notDecorate = returnType.getMethod().getAnnotation(NotDecorate.class);
		if (notDecorate != null) {
			return false;
		}
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
		if("OPTIONS".equals(request.getMethod())) {
			response.setStatusCode(HttpStatus.OK);
		}
		//封装返回结果
		if (body == null) {
			return ApiResult.result();
		}
		if (ApiResult.class.isInstance(body)) {
			return  body;
		}
		if (Collection.class.isInstance(body)) {
			Collection<?> ction = (Collection<?>) body;
			List<?> list = null;
			if (ction instanceof List) {
				list = (List<?>) ction;
			} else {
				list = Lists.newArrayList(ction);
			}
			return ApiResult.result(list);
		}
		if (ModelAndView.class.isInstance(body)) {
			ModelAndView mv = (ModelAndView) body;
			return mv;
		}
		if(Exception.class.isInstance(body)){
			if(InvalidGrantException.class.isInstance(body)){
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				return ApiResult.result(HttpStatus.UNAUTHORIZED.value(),((InvalidGrantException) body).getMessage());
			}else if(CustomException.class.isInstance(body)){
				return (CustomException)body;
			}
			Throwable throwable=((Exception) body).getCause();
			if(Checker.BeNotNull(throwable)){
				if(throwable instanceof UsernameNotFoundException){
					return ApiResult.result(HttpStatus.UNAUTHORIZED.value(),((UsernameNotFoundException) body).getMessage());
				}
			}
			return ApiResult.result(-1);
		}
		return ApiResult.result(body);
	}

}
