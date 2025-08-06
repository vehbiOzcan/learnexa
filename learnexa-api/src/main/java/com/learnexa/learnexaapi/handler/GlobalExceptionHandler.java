package com.learnexa.learnexaapi.handler;

import com.learnexa.learnexaapi.entity.RootEntity;
import com.learnexa.learnexaapi.exception.ApiError;
import com.learnexa.learnexaapi.exception.BaseException;
import com.learnexa.learnexaapi.exception.ExceptionInfo;
import com.learnexa.learnexaapi.exception.MessageType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<RootEntity<ApiError>> handleBaseException(BaseException e, WebRequest request) {
        MessageType messageType = e.getMessageType();
        return ResponseEntity.status(messageType.getStatus()).body(
                RootEntity.error(customApiError(
                        messageType.getStatus(),
                        request,
                        e.getMessage()
                ),messageType.getStatus())
        );
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError<Map<String, List<String>>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        Map<String, List<String>> errors = new HashMap<>();

        for(ObjectError objectError : e.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) objectError).getField();
            if(errors.containsKey(fieldName)) {
                errors.put(fieldName,addMapValue(errors.get(fieldName),objectError.getDefaultMessage()));
            }else {
                errors.put(fieldName,addMapValue(new ArrayList<String>(),objectError.getDefaultMessage()));
            }
        }

        return ResponseEntity.badRequest().body(customApiError(HttpStatus.BAD_REQUEST,request,errors));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(customApiError(
                HttpStatus.FORBIDDEN,
                request,
                MessageType.FORBIDDEN.getMessage()));
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(customApiError(
                HttpStatus.UNAUTHORIZED,
                request,
                MessageType.UNAUTHORIZED.getMessage()));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ApiError> handleException(Exception e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(customApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                request,
                MessageType.INTERNAL_SERVER_ERROR.getMessage()));
    }


    private List<String> addMapValue(List<String> list, String value) {
        list.add(value);
        return list;
    }

    private <T> ApiError<T> customApiError(HttpStatus status, WebRequest request, T message) {
        ApiError<T> apiError = new ApiError<>();
        apiError.setStatus(status.value());

        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setHostname(getHostname());
        exceptionInfo.setPath(request.getDescription(false).substring(4));
        exceptionInfo.setTimeStamp(new Date());
        exceptionInfo.setMessage(message);

        apiError.setExceptionInfo(exceptionInfo);

        return apiError;
    }

    private String getHostname(){
        try{
            /// InetAddress ile biz hostname hostaddress gibi bilgilere erişebiliriz
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            System.out.println("Hostname alınırken bir hata oluştu: " + e.getMessage());
        }
        return "unknown";
    }

}
