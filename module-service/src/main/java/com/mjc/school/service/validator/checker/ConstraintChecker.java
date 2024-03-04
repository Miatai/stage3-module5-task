package com.mjc.school.service.validator.checker;

import java.lang.annotation.Annotation;

public interface ConstraintChecker<T extends Annotation> {

    boolean check(Object value, T constraint, Annotation mainAnnotation);

    Class<T> getType();
}
