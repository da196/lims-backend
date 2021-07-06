/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils.advice;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tz.go.tcra.lims.utils.ErrorResponse;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author emmanuel.mfikwa
 */
@ControllerAdvice
public class RestExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ErrorResponse apiError = new ErrorResponse(status.value(), errors.toString(), System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.BAD_REQUEST, false, errors.toString(), apiError);
        return new ResponseEntity<>(response, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponse apiError = new ErrorResponse(status.value(), ex.getLocalizedMessage(), System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.METHOD_NOT_ALLOWED, false, ex.getLocalizedMessage(), apiError);
        return new ResponseEntity<>(response, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponse apiError = new ErrorResponse(status.value(), ex.getLocalizedMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponse apiError = new ErrorResponse(status.value(), ex.getLocalizedMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponse apiError = new ErrorResponse(status.value(), ex.getLocalizedMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponse apiError = new ErrorResponse(status.value(), ex.getLocalizedMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponse apiError = new ErrorResponse(status.value(), ex.getLocalizedMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponse apiError = new ErrorResponse(status.value(), ex.getLocalizedMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(apiError, status);
    }
}
