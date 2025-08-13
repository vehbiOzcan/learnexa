package com.learnexa.learnexaapi.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RootEntity<T> {

    private boolean success;
    private HttpStatus status;
    private int statusCode;
    private T data;


    public static <T> ResponseEntity<RootEntity<T>> ok(T data) {

        RootEntity<T>  result = new RootEntity<T>();

        result.setSuccess(true);
        result.setStatus(HttpStatus.OK);
        result.setStatusCode(HttpStatus.OK.value());
        result.setData(data);

        return ResponseEntity.ok().body(result);
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
