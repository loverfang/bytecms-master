package net.bytecms.core.handler;

import com.google.common.base.Throwables;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: GlobalExceptionHander
 * @Author: LG
 * @Date: 2019/5/22 13:58
 * @Version: 1.0
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ApiResult handleResourceNotFoundException(MethodArgumentNotValidException e) {
        List<ObjectError> errors=e.getBindingResult().getAllErrors();
        if(Checker.BeNotEmpty(errors)){
            return  ApiResult.result(7000,errors.get(0).getDefaultMessage());
        }
        return ApiResult.result(-1);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult validationErrorHandler(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
        .stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
        if(Checker.BeNotEmpty(errors)){
           return ApiResult.result(7000,errors.get(0));
        }
        return ApiResult.result(-1);
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ApiResult maxUploadSizeException(MaxUploadSizeExceededException ex) {
        log.error(ex.getMessage());
        return ApiResult.result(20027);
    }

    @ExceptionHandler
    public ApiResult handle(Exception ex) {
        log.error(ex.getMessage());
        ApiResult result = adornerExcepiton(ex);
        return result;
    }


    private ApiResult adornerExcepiton(Exception ex){
        if (CustomException.class.isInstance(ex)) {
            return ((CustomException) ex).getR();
        }
        else{
            String message= Checker.BeNotBlank(ex.getMessage())?ex.getMessage():Throwables.getStackTraceAsString(ex);
            log.error(message);
            return ApiResult.result(-1);
        }
    }
}
