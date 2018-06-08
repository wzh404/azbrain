package com.blueocean.azbrain.controller;

import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value=MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultObject handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String errorMsg = exception.getBindingResult().getFieldErrors().stream()
                .map(this::getErrorMessage)
                .findFirst()
                .orElse(exception.getMessage());
        logger.error(errorMsg);
        return ResultObject.fail(ResultCode.BAD_REQUEST.getCode(), errorMsg);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ResultObject handleIllegalArgumentException(IllegalArgumentException e){
        return ResultObject.fail(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
    }

    private String getErrorMessage(FieldError fieldError){
        StringBuilder errorMessage = new StringBuilder(fieldError.getField());
        errorMessage.append("-");
        errorMessage.append(fieldError.getDefaultMessage());
        return  errorMessage.toString();
    }
}
