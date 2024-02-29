package com.mjc.school.service.exceptions;

public class ResourceConflictServiceException extends ServiceException{
    public ResourceConflictServiceException(ServiceErrorCode serviceErrorCode, final String details) {
        super(serviceErrorCode, details);
    }
}
