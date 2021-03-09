package net.bytecms.core.annotation;

import net.bytecms.core.constants.InputTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDefault {
	String  fieldNote() default "";
	InputTypeEnum inputType() default InputTypeEnum.TEXT;
	boolean visibleSwitch() default true; //是否显示必填switch
	boolean visibleCheck() default false;
	boolean initCheck() default false; //默认不选中
	boolean initSwitch() default false;// 默认不选中
}
