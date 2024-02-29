package com.mjc.school.service.exceptions;

public class SortException extends ServiceException{
    public SortException(ServiceErrorCode serviceErrorCode, final String details) {
        super(serviceErrorCode, details);
    }
}
