package com.mjc.school.service.filter;


import com.mjc.school.repository.filter.SearchCriteria;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSearchFilterMapper<T> {
    public static final String SEARCH_DELIMITER = ":";
//    protected List<String> validFilterFields;

    public abstract List<SearchCriteria> map(T searchFilterRequest);

    protected List<SearchCriteria> createSearchCriteria(List<String> filters) {
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        for(String filter : filters){
            String[] splitFilter = filter.split(SEARCH_DELIMITER);
            String field = splitFilter[0].trim();
            String value = splitFilter[1].trim();
//            if(!isFieldValid(field)){
//                throw new FilterException(ServiceErrorCode.INVALID_FIELD_FOR_SORTING, field);
//            }
            searchCriteriaList.add(new SearchCriteria(field, value));
        }
        return searchCriteriaList;
    }

//    protected boolean isFieldValid(String field){
//        return validFilterFields.stream().anyMatch(validField -> validField.equalsIgnoreCase(field));
//    }
}
