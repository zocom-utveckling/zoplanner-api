package com.zo.webapi.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActivityType {
    MEETING("meeting"),
    LESSON("lesson"),
    REVIEW("review"),
    PREPARATION("preparation"),
    OTHER("other");

    private final String apiValue;

    ActivityType(String apiValue) {
        this.apiValue = apiValue;
    }

    @JsonValue
    public String getApiValue() {
        return apiValue;
    }
    // Så att enumen kan matcha mot både lower och uppercase beroende på vad frontend skickar
    @JsonCreator
    public static ActivityType fromValue(String value) {
        if(value == null) return null;
        for (ActivityType type : values()) {
            if(type.apiValue.equalsIgnoreCase(value)) return type;
        }
        throw new IllegalArgumentException("Invalid ActivityType: " + value);
    }
}
