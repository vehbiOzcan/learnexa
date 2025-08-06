package com.learnexa.learnexaapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionInfo<T> {
    private String hostname;
    private String path;
    private Date timeStamp;
    private T message;
}
