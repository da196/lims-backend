/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.utils.AppUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author emmanuel.mfikwa
 */
@Component
@Log
public class LoggingServiceImpl implements LoggingService {

    @Autowired
    private AppUtility app;

    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> parameters = buildParametersMap(httpServletRequest);

        if (!httpServletRequest.getRequestURI().contains("/v1/authenticate") &&
                !httpServletRequest.getRequestURI().contains("/v1/authenticate2")) {

            stringBuilder.append("REQUEST ");
            stringBuilder.append("source=[").append(httpServletRequest.getRemoteAddr()).append("] ");
            stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
            stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
//            stringBuilder.append("id=[ ").append(app.getUser().getId()).append(" ] ");
            //stringBuilder.append("headers=[").append(buildHeadersMap(httpServletRequest)).append("] ");

            if (!parameters.isEmpty()) {
                stringBuilder.append("parameters=[").append(parameters).append("] ");
            }

            if (body != null) {

                stringBuilder.append("body=[" + body + "]");
            }

            log.info(stringBuilder.toString());
        }

    }

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        StringBuilder stringBuilder = new StringBuilder();

        if (!httpServletRequest.getRequestURI().contains("/v1/authenticate") &&
                !httpServletRequest.getRequestURI().contains("/v1/authenticate2")) {

            stringBuilder.append("RESPONSE ");
            stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
            stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
            //stringBuilder.append("responseHeaders=[").append(buildHeadersMap(httpServletResponse)).append("] ");
            stringBuilder.append("responseBody=").append(body);
//            log.info(stringBuilder.toString());
        }
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }
}
