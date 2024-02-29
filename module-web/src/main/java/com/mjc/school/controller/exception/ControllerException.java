package com.mjc.school.controller.exception;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class ControllerException extends RuntimeException{
    private final ControllerErrorCode controllerErrorCode;
    private final String cause;
    private final MessageSource messageSource;


    public ControllerException(ControllerErrorCode controllerErrorCode, String cause) {
        this.controllerErrorCode = controllerErrorCode;
        this.cause = cause;
        this.messageSource = ControllerExceptionMessageSource.getMessageSource();
    }

    public String getErrorCode(){
        return controllerErrorCode.getErrorCode();
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage(Locale.getDefault());
    }

    public String getLocalizedMessage(Locale locale) {
        String message = messageSource.getMessage("message." + controllerErrorCode.getErrorCode(), new Object[]{}, locale);
        if(cause == null){
            return message;
        }
        return String.format(message, cause);
    }
}
