package com.mjc.school.service.exceptions;

public class NotFoundException extends ServiceException {
    public NotFoundException(ServiceErrorCode serviceErrorCode, final String[] details) {
        super(serviceErrorCode, details);
    }
}
