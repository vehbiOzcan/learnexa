package com.learnexa.learnexaapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {

    private MessageType messageType;
    private String extraInfo;

    public ErrorMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    public String prepareErrorMessage(){

        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append(messageType.getCode() + " - ");
        messageBuilder.append(messageType.getTitle() + " : ");
        messageBuilder.append(messageType.getMessage());

        if (extraInfo != null && !extraInfo.isEmpty()) messageBuilder.append(" [ " + extraInfo + " ] ");

        return messageBuilder.toString();

    }

}
