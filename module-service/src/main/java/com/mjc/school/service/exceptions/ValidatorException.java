package com.mjc.school.service.exceptions;

public class ValidatorException extends ServiceException {
    public ValidatorException(ServiceErrorCode serviceErrorCode, final String details) {
        super(serviceErrorCode, details);
    }
}
