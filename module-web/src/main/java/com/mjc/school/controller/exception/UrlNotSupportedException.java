package com.mjc.school.controller.exception;

public class UrlNotSupportedException extends ControllerException{
    public UrlNotSupportedException(ControllerErrorCode controllerErrorCode, String cause) {
        super(controllerErrorCode, cause);
    }
}
