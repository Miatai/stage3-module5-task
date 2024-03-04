package com.mjc.school.service.exceptions;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class ServiceException extends RuntimeException {
    protected final ServiceErrorCode serviceErrorCode;
    protected final String[] cause;
    protected final MessageSource messageSource;

    public ServiceException(ServiceErrorCode serviceErrorCode, final String[] cause) {
        this.serviceErrorCode = serviceErrorCode;
        this.cause = cause;
        this.messageSource = ServiceExceptionMessageSource.getMessageSource();
    }

    public String getErrorCode(){
        return serviceErrorCode.getErrorCode();
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage(Locale.getDefault());
    }

    public String getLocalizedMessage(Locale locale) {
        String message = messageSource.getMessage("message." + serviceErrorCode.getErrorCode(), new Object[]{}, locale);
        if(cause == null){
            return message;
        }
        return String.format(message, (Object[]) cause);
    }
}