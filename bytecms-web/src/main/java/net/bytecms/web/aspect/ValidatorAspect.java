//package com.org.wechat.web.aspect;
//
//import com.org.wechat.core.handler.CustomException;
//import com.org.wechat.core.utils.ApiResult;
//import com.org.wechat.core.utils.Checker;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Path;
//import javax.validation.Validator;
//import java.util.Set;
//
//
//@Component
//@Aspect
//public class ValidatorAspect {
//
//    @Autowired
//    Validator validator;
//
//    @Around("execution(* com.org.wechat.web.controller..*.*(@javax.validation.Valid (*),..))")
//    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
//        Object[] args = pjp.getArgs();
//        if(Checker.BeNotEmpty(args)){
//            for(Object arg:args){
//                Set<ConstraintViolation<Object>> constraintViolations = validator.validate(arg);
//                if(Checker.BeNotEmpty(constraintViolations)){
//                    for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
//                        Path property = constraintViolation.getPropertyPath();
//                        String name = property.iterator().next().getName();
//                        throw new CustomException(ApiResult.result(7000,constraintViolation.getMessage()));
//                    }
//                }
//            }
//        }
//        return pjp.proceed();
//    }
//}
