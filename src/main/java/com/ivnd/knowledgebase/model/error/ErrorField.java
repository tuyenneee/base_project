package com.ivnd.knowledgebase.model.error;

import lombok.Getter;

@Getter
public class ErrorField {
    private final String objectName;
    private final String fieldName;
    private final String message;

    public ErrorField(String objectName, String fieldName, String message) {
        this.objectName = objectName;
        this.fieldName = fieldName;
        this.message = message;
    }

}
