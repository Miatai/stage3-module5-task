package com.mjc.school.service.validator;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public interface Validator {
    List<String> validate(Object object, Annotation mainAnnotation) throws NoSuchMethodException;
}
