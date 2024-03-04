package com.mjc.school.service.validator.checker;

import org.springframework.stereotype.Component;

import com.mjc.school.service.validator.constraint.Min;

import java.lang.annotation.Annotation;

@Component
public class MinConstraintChecker implements ConstraintChecker<Min> {

    @Override
    public boolean check(final Object value, final Min constraint, Annotation mainAnnotation) {
        return !(value instanceof Number number) || number.longValue() >= constraint.value();
    }

    @Override
    public Class<Min> getType() {
        return Min.class;
    }
}
