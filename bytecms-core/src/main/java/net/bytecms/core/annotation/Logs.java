package net.bytecms.core.annotation;

import net.bytecms.core.enumerate.LogModule;
import net.bytecms.core.enumerate.LogOperation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logs {
	String  operation() default "";

	LogModule module() default LogModule.DEFAULT;

	LogOperation operaEnum() default LogOperation.DEFAULT;
}
