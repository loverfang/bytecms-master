package net.bytecms.core.annotation;

import net.bytecms.core.constants.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheClear {

	String  key() default "";

	String[] keys() default {};

	Class<?>[] clzs() default {};

	String value() default Constants.cacheNameClear;
}
