package com.mjc.school.service.exceptions;

public enum ServiceErrorCode {
    NEWS_ID_DOES_NOT_EXIST("000001", "News with id %d does not exist."),
    AUTHOR_ID_DOES_NOT_EXIST("000002", "Author with id %d does not exist."),
    TAG_ID_DOES_NOT_EXIST("000003", "Tag with id %d does not exist."),
    COMMENT_ID_DOES_NOT_EXIST("000004", "Comment with id %d does not exist."),
    AUTHOR_DOES_NOT_EXIST_FOR_NEWS_ID("000005", "Author not found for news with id %d."),
    INVALID_FIELD_FOR_SORTING("000014", "Invalid field for sorting. Field: %s"),
    INVALID_FIELD_FOR_SEARCHING("000015", "Invalid field for searching. Field: %s"),
    INVALID_SORT_ORDER("000018", "Wrong sort order: Sort order: %s"),
    VALIDATION("000013", "Validation failed: %s"),
    AUTHOR_CONFLICT("000021", "Author has a persistence conflict due to entity id existence."),
    UNEXPECTED_ERROR("000050", "Unexpected error happened on server."),
    NEWS_CONFLICT("000031", "News has a persistence conflict due to entity id existence."),
    TAG_CONFLICT("000041", "Tag has a persistence conflict due to entity id existence."),
    COMMENT_CONFLICT("000051", "Comment has a persistence conflict due to entity id existence.");

    private final String errorCode;
    private final String errorMessage;

    ServiceErrorCode(String errorCode, String message) {
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    public String getMessage() {
        return "message." + errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
