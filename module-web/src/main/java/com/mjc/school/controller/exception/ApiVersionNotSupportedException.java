package com.mjc.school.controller.exception;

public class ApiVersionNotSupportedException extends ControllerException {
    public ApiVersionNotSupportedException(ControllerErrorCode controllerErrorCode, String cause) {
        super(controllerErrorCode, cause);
    }
}
