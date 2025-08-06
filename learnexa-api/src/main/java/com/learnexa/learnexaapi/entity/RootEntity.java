package com.learnexa.learnexaapi.entity;

import com.learnexa.learnexaapi.exception.ApiError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RootEntity<T> {

    private boolean success;
    private HttpStatus status;
    private int statusCode;
    private T data;


    public static <T> RootEntity<T> ok(T data) {

        RootEntity<T>  result = new RootEntity<T>();

        result.setSuccess(true);
        result.setStatus(HttpStatus.OK);
        result.setStatusCode(HttpStatus.OK.value());
        result.setData(data);

        return result;
    }

    public static <T> ResponseEntity<RootEntity<T>> ok(T data, HttpStatus status) {

        RootEntity<T>  result = new RootEntity<T>();

        result.setSuccess(true);
        if(status == null) {
            result.setStatus(HttpStatus.OK);
            result.setStatusCode(HttpStatus.OK.value());
        }
        else {
            result.setStatus(status);
            result.setStatusCode(status.value());
        }
        result.setData(data);

        return ResponseEntity.status(status).body(result);
    }

    public static <T> RootEntity<T> error(T error, HttpStatus status) {
        RootEntity<T>  result = new RootEntity<T>();

        result.setSuccess(false);
        result.setStatus(status);
        result.setStatusCode(status.value());
        result.setData(error);

        return result;
    }

}
