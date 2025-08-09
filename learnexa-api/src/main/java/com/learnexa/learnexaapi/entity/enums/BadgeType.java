package com.learnexa.learnexaapi.entity.enums;

public enum BadgeType {
    CONSISTENT("C"),
    NIGHT_OWL("N"),
    EARLY_BIRD("E"),
    FOCUSED("F"),
    MARATHONER("M"),
    STAR_COLLECTOR("S"),
    SOCIAL_BUTTERFLY("U"),
    PERFECTIONIST("P"),
    POMODORO_MASTER("X");

    private final String code;

    BadgeType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

