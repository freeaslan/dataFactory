package com.esign.service.dfplatform.exception;

import com.esign.service.dfplatform.base.DfplatformResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @Author: huangtai
 * @Description: 异常处理
 * @Date: 2021/6/14 18:14
 */
@RestControllerAdvice("com.esign.service.dfplatform.controller")
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST) //设置状态码为 400
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public DfplatformResult<String> paramExceptionHandler(MethodArgumentNotValidException e) {

        DfplatformResult<String> result = new DfplatformResult<>();
        result.setCode(-1);
        BindingResult exceptions = e.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                // 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
                FieldError fieldError = (FieldError) errors.get(0);
                result.setMessage(fieldError.getDefaultMessage());
                return result;
            }
        }
        result.setMessage("请求参数错误");
        return result;
    }
}
