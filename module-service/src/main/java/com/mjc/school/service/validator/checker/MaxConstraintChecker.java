package com.mjc.school.service.validator.checker;

import org.springframework.stereotype.Component;

import com.mjc.school.service.validator.constraint.Max;

@Component
public class MaxConstraintChecker implements ConstraintChecker<Max> {

    @Override
    public boolean check(final Object value, final Max constraint) {
        return !(value instanceof Number number) || number.longValue() <= constraint.value();
    }

    @Override
    public Class<Max> getType() {
        return Max.class;
    }
}
