package com.mjc.school.service.filter;


import com.mjc.school.repository.filter.SearchCriteria;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSearchFilterMapper<T> {
    public static final String SEARCH_DELIMITER = ":";

    public abstract List<SearchCriteria> map(T searchFilterRequest);

    protected List<SearchCriteria> createSearchCriteria(List<String> filters) {
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        for(String filter : filters){
            String[] splitFilter = filter.split(SEARCH_DELIMITER);
            String field = splitFilter[0].trim();
            String value = splitFilter[1].trim();
            searchCriteriaList.add(new SearchCriteria(field, value));
        }
        return searchCriteriaList;
    }
}
