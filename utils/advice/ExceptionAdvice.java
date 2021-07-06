/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils.advice;

//import javax.servlet.ServletException;

import com.fasterxml.jackson.core.JsonParseException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tz.go.tcra.lims.utils.ErrorResponse;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.*;

/**
 * @author emmanuel.mfikwa
 */
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> generalExceptionResponse(GeneralException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        err.setMessage(exc.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.FAILURE, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//        @ExceptionHandler(Exception.class)
//	public ResponseEntity<Object> generalExceptionResponse(Exception exc) {
//                exc.printStackTrace();
//		ErrorResponse err = new ErrorResponse();
//		err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//		err.setMessage("INTERNAL SERVER ERROR");
//		err.setTimestamp(System.currentTimeMillis());
//                Response<ErrorResponse> response=new Response<>(ResponseCode.FAILURE,false,exc.getMessage(),err);
//		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//	}

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> dataNotFoundExceptionResponse(DataNotFoundException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.FAILED_DEPENDENCY.value());
        err.setMessage(exc.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.NO_RECORD_FOUND, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.FAILED_DEPENDENCY);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> badRequestExceptionResponse(BadRequestException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setMessage(exc.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.BAD_REQUEST, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Object> badRequestExceptionResponse(MalformedJwtException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setMessage(exc.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.BAD_REQUEST, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<Object> badRequestExceptionResponse(JsonParseException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setMessage(exc.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.BAD_REQUEST, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<Object> operationNotAllowedExceptionResponse(OperationNotAllowedException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.PRECONDITION_FAILED.value());
        err.setMessage(exc.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.CONSTRAINT_VIOLATION, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> badRequestExceptionResponse(NullPointerException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        err.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.FAILURE, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> authenticationExceptionResponse(AuthException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.UNAUTHORIZED.value());
        err.setMessage(exc.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.UNAUTHORIZED, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

//	@ExceptionHandler
//	public Response<ErrorResponse> servletExceptionResponse(ServletException exc) {
//
//		ErrorResponse err = new ErrorResponse();
//		err.setStatus(HttpStatus.UNAUTHORIZED.value());
//		err.setMessage(exc.getMessage());
//		err.setTimestamp(System.currentTimeMillis());
//
//		return new Response<>(ResponseCode.UNAUTHORIZED, false, exc.getMessage(), err);
//	}

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> forbiddenExceptionResponse(ForbiddenException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.FORBIDDEN.value());
        err.setMessage(exc.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.RESTRICTED_ACCESS, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> forbiddenExceptionResponse(AccessDeniedException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.FORBIDDEN.value());
        err.setMessage(exc.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.RESTRICTED_ACCESS, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Object> duplicateExceptionResponse(DuplicateException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.ALREADY_REPORTED.value());
        err.setMessage(exc.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.DUPLICATE, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }

//	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//	public ResponseEntity<Object> pageNotFoundExceptionResponse(HttpRequestMethodNotSupportedException exc) {
//
//		ErrorResponse err = new ErrorResponse();
//		err.setStatus(HttpStatus.NOT_FOUND.value());
//		err.setMessage(exc.getMessage());
//		err.setTimestamp(System.currentTimeMillis());
//
//		return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
//	}

//	@ExceptionHandler()
//	public ResponseEntity<Object> generalExceptionResponse(Exception exc) {
//
//		ErrorResponse err = new ErrorResponse();
//		err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//		err.setMessage(exc.getMessage());
//		err.setTimestamp(System.currentTimeMillis());
//
//		return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
//	}

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> authenticationExceptionResponse(AuthenticationException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.UNAUTHORIZED.value());
        err.setMessage(exc.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.UNAUTHORIZED, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> authenticationExceptionResponse(ExpiredJwtException exc) {

        ErrorResponse err = new ErrorResponse();
        err.setStatus(HttpStatus.FORBIDDEN.value());
        err.setMessage(exc.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        Response<ErrorResponse> response = new Response<>(ResponseCode.RESTRICTED_ACCESS, false, exc.getMessage(), err);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

//        @ExceptionHandler(MethodArgumentNotValidException.class)
//        public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
//            Map<String, String> errors = new HashMap<>();
//            System.out.println("Argument Error");
//            ex.getBindingResult().getFieldErrors().forEach(error -> 
//                    
//                
//                errors.put(error.getField(), error.getDefaultMessage())
//            );
//
//            ErrorResponse err = new ErrorResponse();
//            err.setStatus(HttpStatus.FAILED_DEPENDENCY.value());
//            err.setMessage(errors.toString());
//            err.setTimestamp(System.currentTimeMillis());
//            Response<ErrorResponse> response=new Response<>(ResponseCode.BAD_REQUEST,false,errors.toString(),err);
//            return new ResponseEntity<>(response, HttpStatus.FAILED_DEPENDENCY);
//        }
}
