package com.mjc.school.service.validator;

import com.mjc.school.service.exceptions.ServiceErrorCode;
import com.mjc.school.service.exceptions.ValidatorException;
import com.mjc.school.service.validator.checker.ConstraintChecker;
import com.mjc.school.service.validator.constraint.Constraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Component
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ValidatorImpl implements Validator {

    private final Map<Class<? extends Annotation>, ConstraintChecker> checkersMap;

    @Autowired
    public ValidatorImpl(final List<ConstraintChecker> checkers) {
        this.checkersMap = checkers.stream().collect(toMap(ConstraintChecker::getType, Function.identity()));
    }

    public List<String> validate(final Object object, Annotation mainAnnotation) {
        if (object == null) {
            return Collections.emptyList();
        }
        var violations = new ArrayList<String>();
        for (var declaredField : object.getClass().getDeclaredFields()) {
            validateField(violations, declaredField, object, mainAnnotation);
        }
        return violations;
    }

    private void validateObject(List<String> violations, final Object object, Annotation mainAnnotation) {
        if (object == null) {
            return;
        }
        for (var declaredField : object.getClass().getDeclaredFields()) {
            validateField(violations, declaredField, object, mainAnnotation);
        }
    }

    private void validateField(
            final List<String> violations, final Field field, final Object instance, Annotation mainAnnotation) {
        for (var declaredAnnotation : field.getDeclaredAnnotations()) {
            var annotationType = declaredAnnotation.annotationType();
            if (annotationType.isAnnotationPresent(Constraint.class)) {
                try {
                    if (field.trySetAccessible() && field.canAccess(instance)) {
                        var value = field.get(instance);
                        var checker = checkersMap.get(annotationType);
                        if (checker != null && !checker.check(value, annotationType.cast(declaredAnnotation), mainAnnotation)) {
//                            throw new ValidatorException(ServiceErrorCode.INVALID_SORT_AND_ORDER_FIELD_CONSTRAINT, new String[]{annotationType.getSimpleName(),value.toString(), field.getName()});
                            //change this to throwing exception. and change method's return type to void??
                            violations.add(annotationType.getSimpleName());
                            violations.add(value.toString());
                            violations.add(field.getName());
                        }
                        validateObject(violations, value, mainAnnotation);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
