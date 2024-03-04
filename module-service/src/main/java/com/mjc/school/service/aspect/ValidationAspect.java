package com.mjc.school.service.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Stream;

import com.mjc.school.service.validator.ValidFields;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mjc.school.service.exceptions.ServiceErrorCode;
import com.mjc.school.service.exceptions.ValidatorException;
import com.mjc.school.service.validator.ConstraintViolation;
import com.mjc.school.service.validator.Valid;
import com.mjc.school.service.validator.Validator;

@Aspect
@Component
public class ValidationAspect {

    private final Validator validator;

    @Autowired
    public ValidationAspect(Validator validator) {
        this.validator = validator;
    }

    @Before("execution(public * *(.., @com.mjc.school.service.validator.Valid (*), ..)) " +
        "|| execution(public * * (.., @com.mjc.school.service.validator.ValidFields (*), ..))")
    public void validateBeforeInvocation(final JoinPoint joinPoint) throws NoSuchMethodException {
        if (joinPoint.getSignature() instanceof MethodSignature signature) {
            var targetMethod = getTargetMethod(joinPoint, signature);
            var args = joinPoint.getArgs();
            var parameterAnnotations = targetMethod.getParameterAnnotations();

            var violations = new ArrayList<String>();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for(Annotation annotation : parameterAnnotations[i]){
                    if(annotation instanceof Valid || annotation instanceof ValidFields){
                        violations.addAll(validator.validate(args[i], annotation));
                    }
                }
            }

            if (!violations.isEmpty()) {
                String[] details = new String[violations.size()];
                throw new ValidatorException(ServiceErrorCode.VALIDATION,
                    violations.toArray(details));
            }
        }
    }

    private Method getTargetMethod(JoinPoint joinPoint, MethodSignature signature) throws NoSuchMethodException {
        Method baseMethod = signature.getMethod();
        return joinPoint.getTarget().getClass()
                .getMethod(baseMethod.getName(), baseMethod.getParameterTypes());
    }
}