
package net.bytecms.core.annotation;

import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Clear {
	Class<? extends HandlerInterceptor>[] value() default {};
}