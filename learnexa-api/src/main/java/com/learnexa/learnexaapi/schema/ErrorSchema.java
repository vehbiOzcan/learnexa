package com.learnexa.learnexaapi.schema;

import com.learnexa.learnexaapi.entity.RootEntity;
import com.learnexa.learnexaapi.exception.ApiError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


public abstract class ErrorSchema extends RootEntity<ApiError> {
}
