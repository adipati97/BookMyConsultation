package com.upgrad.bookmyconsultation.exception;

import java.util.HashMap;
import java.util.Map;

public enum InvalidInputErrorCode implements ErrorCode {
    INVALID_INPUT("INVALID_INPUT", "One or more fields does not contain a valid input");

    private static final Map<String, InvalidInputErrorCode> LOOKUP = new HashMap<String, InvalidInputErrorCode>();

    static {
        for (final InvalidInputErrorCode enumeration : InvalidInputErrorCode.values()) {
            LOOKUP.put(enumeration.getCode(), enumeration);
        }
    }

    private final String code;
    private final String defaultMessage;

    InvalidInputErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDefaultMessage() {
        return this.defaultMessage;
    }
}
