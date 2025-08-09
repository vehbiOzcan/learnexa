package com.learnexa.learnexaapi.entity.enums;

public enum Level {
    BEGINNER("B"),
    ITERMEDIATE("I"),
    ADVANCED("A"),
    EXPERT("E"),
    MASTER("X");

    private final String code;

    Level(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
