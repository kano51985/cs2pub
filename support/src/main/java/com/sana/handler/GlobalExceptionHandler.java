package com.sana.handler;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sana.constants.HttpStatus;
import com.sana.exception.BusinessException;
import com.sana.response.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 全局异常处理器
 * @Version: 1.0
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements ResponseBodyAdvice<Object> {
    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e, HttpServletRequest request) {
        // 记录异常日志
        logErrorRequest(request, e);

        return R.error(HttpStatus.ERROR, "服务器内部错误: " + e.getMessage())
                .put("path", request.getRequestURI());
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public R handleBusinessException(BusinessException e, HttpServletRequest request) {
        // 记录异常日志
        logErrorRequest(request, e);

        return R.error(e.getCode(), e.getMessage())
                .put("detail", e.getDetail())
                .put("path", request.getRequestURI());
    }

    /**
     * 记录异常请求信息
     */
    private void logErrorRequest(HttpServletRequest request, Exception e) {
        Map<String, Object> requestInfo = new HashMap<>();

        try {
            // 请求基本信息
            requestInfo.put("path", request.getRequestURI());
            requestInfo.put("method", request.getMethod());
            requestInfo.put("query", request.getQueryString());
            requestInfo.put("remoteAddr", request.getRemoteAddr());

            // 请求头
            Map<String, String> headers = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headers.put(headerName, request.getHeader(headerName));
            }
            requestInfo.put("headers", headers);

            // 请求参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            requestInfo.put("parameters", parameterMap);

            // 异常信息
            Map<String, Object> errorInfo = new HashMap<>();
            errorInfo.put("exception", e.getClass().getName());
            errorInfo.put("message", e.getMessage());
            errorInfo.put("stackTrace", e.getStackTrace());

            requestInfo.put("error", errorInfo);

            // 使用JSON格式记录日志
            log.error("异常请求信息: {}", requestInfo);
        } catch (Exception ex) {
            log.error("记录异常请求信息失败", ex);
        }
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 可以在这里记录正常请求的日志
        if (request instanceof ServerHttpRequest) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest servletRequest = attributes.getRequest();
                // 可以记录请求日志
            }
        }
        return body;
    }
}
