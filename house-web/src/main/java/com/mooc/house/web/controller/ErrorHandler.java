package com.mooc.house.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 *添加freemark统一异常处理器
 */
@ControllerAdvice
public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler
    public String error500(HttpServletRequest request, Exception e) {
        logger.error(e.getMessage(), e);
        logger.error(request.getRequestURI() + "encounter 500");
        return "error/500";
    }

}
