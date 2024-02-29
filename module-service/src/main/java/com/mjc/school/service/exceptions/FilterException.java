package com.mjc.school.service.exceptions;

public class FilterException extends ServiceException{
    public FilterException(ServiceErrorCode serviceErrorCode, final String details) {
        super(serviceErrorCode, details);
    }
}
