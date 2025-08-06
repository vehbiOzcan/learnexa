package com.learnexa.learnexaapi.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private MessageType messageType;

    public BaseException(ErrorMessage errorMessage) {
        super(errorMessage.prepareErrorMessage());
        this.messageType = errorMessage.getMessageType();
    }
}
