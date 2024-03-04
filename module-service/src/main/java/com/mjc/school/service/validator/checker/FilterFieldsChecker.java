package com.mjc.school.service.validator.checker;

import com.mjc.school.service.validator.ValidFields;
import com.mjc.school.service.validator.constraint.FilterFields;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.mjc.school.service.filter.BaseSearchFilterMapper.SEARCH_DELIMITER;

@Component
public class FilterFieldsChecker implements ConstraintChecker<FilterFields>{
    @Override
    public boolean check(Object value, FilterFields constraint, Annotation mainAnnotation) {
        ValidFields validFieldsAnnotation;
        String[] validFields = null;
        if(mainAnnotation instanceof ValidFields){
            validFieldsAnnotation = (ValidFields) mainAnnotation;
            validFields = validFieldsAnnotation.fields();
        }
        for (String criteria : (List<String>) value) {
            String[] splitCriteria = criteria.split(SEARCH_DELIMITER);
            if (splitCriteria.length < 2 || !isFieldValid(splitCriteria[0], validFields)) {
                return false;
            }
        }
        return true;
    }

    private boolean isFieldValid(String field, String[] validFields){
        if(validFields == null){
            return false;
        }
        for (String validField : validFields){
            if(validField.equalsIgnoreCase(field.trim())){
                return  true;
            }
        }
        return false;
    }

    @Override
    public Class<FilterFields> getType() {
        return FilterFields.class;
    }
}
