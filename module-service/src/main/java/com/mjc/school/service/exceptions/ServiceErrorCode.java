package com.mjc.school.service.exceptions;

public enum ServiceErrorCode {
    NEWS_ID_DOES_NOT_EXIST("000001"),
    AUTHOR_ID_DOES_NOT_EXIST("000002"),
    TAG_ID_DOES_NOT_EXIST("000003"),
    COMMENT_ID_DOES_NOT_EXIST("000004"),
    AUTHOR_DOES_NOT_EXIST_FOR_NEWS_ID("000005"),
    VALIDATION("000013"),
    CONSTRAINT_MESSAGE_CODE("000014"),
    AUTHOR_CONFLICT("000021"),
    UNEXPECTED_ERROR("000050"),
    NEWS_CONFLICT("000031"),
    TAG_CONFLICT("000041"),
    COMMENT_CONFLICT("000051");

    private final String errorCode;

    ServiceErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
