/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import tz.go.tcra.lims.utils.service.LoggingService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;

/**
 * @author emmanuel.mfikwa
 */
@ControllerAdvice
public class RequestAdviceAdapter extends RequestBodyAdviceAdapter {

    @Autowired
    LoggingService loggingService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {

        loggingService.logRequest(httpServletRequest, body);

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
