package com.ivnd.knowledgebase.model.error;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    private final String error;
    private final String message;
    private final List<ErrorField> errorFields = new ArrayList<>();

    public ErrorResponse(String code, String message) {
        this.error = code;
        this.message = message;
    }

    public void addFieldError(ErrorField fieldError) {
        errorFields.add(fieldError);
    }


    @Override
    public String toString() {
        return "ErrorResponse{"
                + '\''
                + ", error='"
                + error
                + '\''
                + ", message='"
                + message
                + '}';
    }
}
