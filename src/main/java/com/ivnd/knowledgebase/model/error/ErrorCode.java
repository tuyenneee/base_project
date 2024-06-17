package com.ivnd.knowledgebase.model.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    COMMON_001_ENTITY_NOT_FOUND("COMMON-001", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, HttpStatus status) {
        this.code = code;
        this.message = this.name();
        this.status = status;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public HttpStatus status() {
        return status;
    }
}
