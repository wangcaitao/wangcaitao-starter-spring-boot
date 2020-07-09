package com.wangcaitao.starter.web;

import com.wangcaitao.common.constant.HttpStatusEnum;
import com.wangcaitao.common.entity.Result;
import com.wangcaitao.common.exception.ResultException;
import com.wangcaitao.common.util.HttpServletRequestUtils;
import com.wangcaitao.common.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * @author wangcaitao
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerConfig {

    /**
     * Exception handler
     *
     * @param request HttpServletRequest
     * @param e       Exception
     * @return Result
     */
    @ExceptionHandler(value = Exception.class)
    public Result<Serializable> handlerException(HttpServletRequest request, Exception e) {
        log.error("un handler exception. request-method: {}, request-url: {}, param: {}", request.getMethod(), request.getServletPath(), HttpServletRequestUtils.getParam(request), e);

        return ResultUtils.error();
    }

    /**
     * RuntimeException handler
     *
     * @param request HttpServletRequest
     * @param e       RuntimeException
     * @return Result
     */
    @ExceptionHandler(value = RuntimeException.class)
    public Result<Serializable> handlerException(HttpServletRequest request, RuntimeException e) {
        log.error("runtime exception. request-method: {}, request-url: {}, param: {}", request.getMethod(), request.getServletPath(), HttpServletRequestUtils.getParam(request), e);

        return ResultUtils.error();
    }

    /**
     * ResultException handler
     *
     * @param request HttpServletRequest
     * @param e       ResultException
     * @return Result
     */
    @ExceptionHandler(value = ResultException.class)
    public Result<Serializable> handlerException(HttpServletRequest request, ResultException e) {
        log.error("result exception. msg: {}, request-method: {}, request-url: {}, param: {}", e.getMsg(), request.getMethod(), request.getServletPath(), HttpServletRequestUtils.getParam(request));

        return ResultUtils.error(e.getCode(), e.getMsg());
    }

    /**
     * NoHandlerFoundException handler
     *
     * @param e NoHandlerFoundException
     * @return Result
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public Result<Serializable> handlerException(NoHandlerFoundException e) {
        log.error("resource not fund. request-method: {}, request-url: {}", e.getHttpMethod(), e.getRequestURL());

        return ResultUtils.error(HttpStatusEnum.NOT_FOUND);
    }

    /**
     * HttpMediaTypeNotSupportedException handler
     *
     * @param request HttpServletRequest
     * @param e       HttpMediaTypeNotSupportedException
     * @return Result
     */
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public Result<Serializable> handlerException(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
        log.error("content-type error. request-method: {}, request-url: {}, request-content-type: {}, support-content-type: {}", request.getMethod(), request.getServletPath(), e.getContentType(), e.getSupportedMediaTypes());

        return ResultUtils.error(HttpStatusEnum.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * HttpMessageNotReadableException handler
     *
     * @param request HttpServletRequest
     * @param e       HttpMessageNotReadableException
     * @return Result
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result<Serializable> handlerException(HttpServletRequest request, HttpMessageNotReadableException e) {
        String msg = "缺少请求参数";
        String message = e.getMessage();
        if (StringUtils.isNotEmpty(message)) {
            if (message.contains(DateTimeParseException.class.getName()) || message.contains(Date.class.getName()) || message.contains(Timestamp.class.getName())) {
                msg = "存在时间格式错误";
            }
        }

        log.error("miss request body. request-method: {}, request-url: {}, msg: {}", request.getMethod(), request.getServletPath(), msg);

        return ResultUtils.error(msg);
    }

    /**
     * HttpRequestMethodNotSupportedException handler
     *
     * @param request HttpServletRequest
     * @param e       HttpRequestMethodNotSupportedException
     * @return Result
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result<Serializable> handlerException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        log.error("method error. request-method: {}, request-url: {}, support-method: {}", request.getMethod(), request.getServletPath(), e.getSupportedMethods());

        return ResultUtils.error(HttpStatusEnum.METHOD_NOT_ALLOWED);
    }

    /**
     * BindException handler
     *
     * @param request HttpServletRequest
     * @param e       BindException
     * @return Result
     */
    @ExceptionHandler(value = BindException.class)
    public Result<Serializable> handlerException(HttpServletRequest request, BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
        String field = fieldError.getField();
        String message = fieldError.getDefaultMessage();

        if (StringUtils.isNotEmpty(message)) {
            if (message.contains(DateTimeParseException.class.getName()) || message.contains(Date.class.getName()) || message.contains(Timestamp.class.getName())) {
                message = field + " 格式错误";
            } else if (message.contains(Boolean.class.getName())) {
                message = field + " 为布尔类型. 支持的值有 0, 1, true, false";
            }
        }

        log.error("param validate error. request-method: {}, request-url: {}, objectName: {}, field: {}, rejectedValue: {}, message: {}", request.getMethod(), request.getServletPath(), fieldError.getObjectName(), field, fieldError.getRejectedValue(), message);

        return ResultUtils.error(message);
    }

    /**
     * MethodArgumentNotValidException handler
     *
     * @param request HttpServletRequest
     * @param e       MethodArgumentNotValidException
     * @return Result
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Serializable> handlerException(HttpServletRequest request, MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
        log.error("param validate error. request-method: {}, request-url: {}, objectName: {}, field: {}, rejectedValue: {}, message: {}", request.getMethod(), request.getServletPath(), fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());

        return ResultUtils.error(fieldError.getDefaultMessage());
    }
}
