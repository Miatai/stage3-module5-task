package com.mjc.school.service.validator.checker;

import com.mjc.school.repository.sorting.SortOrder;
import com.mjc.school.service.validator.ValidFields;
import com.mjc.school.service.validator.constraint.SortFieldsAndOrder;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.mjc.school.service.sort.BaseSortingMapper.SORT_DELIMITER;

@Component
public class SortFieldsAndOrderChecker implements ConstraintChecker<SortFieldsAndOrder> {

    @Override
    public boolean check(Object value, SortFieldsAndOrder constraint, Annotation mainAnnotation) {
        ValidFields validFieldsAnnotation;
        String[] validFields = null;
        if(mainAnnotation instanceof ValidFields){
            validFieldsAnnotation = (ValidFields) mainAnnotation;
            validFields = validFieldsAnnotation.fields();
        }

        for (String sort : (List<String>) value) {
            String[] splitSort = sort.split(SORT_DELIMITER);
            if (splitSort.length != 2 || !SortOrder.isSortOrderExisted(splitSort[1].trim()) || !isFieldValid(splitSort[0], validFields)) {
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
    public Class<SortFieldsAndOrder> getType() {
        return SortFieldsAndOrder.class;
    }
}
