package com.learnexa.learnexaapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError<T> {
    private Integer status;
    private ExceptionInfo<T> exceptionInfo;

}
