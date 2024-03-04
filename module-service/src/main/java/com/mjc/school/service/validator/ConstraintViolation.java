package com.mjc.school.service.validator;

public record ConstraintViolation(
    String constrain,
    String value,
    String field
) {
}
