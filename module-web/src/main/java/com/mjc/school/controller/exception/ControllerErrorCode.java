package com.mjc.school.controller.exception;

public enum ControllerErrorCode {
    URL_NOT_SUPPORTED("100001", "Requested Url not not supported. Url: %s"),
    API_VERSION_NOT_SUPPORTED("100002", "Api version not supported.");

    private final String errorCode;
    private final String errorMessage;

    ControllerErrorCode(String errorCode, String message) {
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    public String getMessage() {
        return "message." + errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
