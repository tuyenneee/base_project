package com.ivnd.knowledgebase.model.error;

import lombok.Getter;

public class ErrorCodeException extends RuntimeException {
    private static final long serialVersionUID = -2231577185500549752L;
    @Getter
    private final ErrorCode errorCode;
    private final String message;
    private String[] params;

    public ErrorCodeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.message();
    }

    public ErrorCodeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
