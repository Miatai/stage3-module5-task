package com.mjc.school.service.sort;


import com.mjc.school.repository.sorting.SortOrder;
import com.mjc.school.repository.sorting.Sorting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseSortingMapper<T> {
    public static final String SORT_DELIMITER = ":";
    protected List<String> validFieldsForSorting;
    protected final static Map<String, SortOrder> DEFAULT_SORTING_MAP = new HashMap<>();

    public abstract List<Sorting> map(T sortingRequest);

    public List<Sorting> getDefaultSorting() {
        return DEFAULT_SORTING_MAP.entrySet().stream()
            .map(e -> new Sorting(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    protected List<Sorting> createSorting(List<String> sortByAndOrder){
        if(sortByAndOrder.isEmpty()){
            return getDefaultSorting();
        }
        List<Sorting> sortingList = new ArrayList<>();
        for(String sorting : sortByAndOrder){
            String[] sortingSplit = sorting.split(SORT_DELIMITER);
            String field = sortingSplit[0].trim();
            String sortType = sortingSplit[1].trim();
//            if(!isFieldValid(field)){
//                throw new SortException(ServiceErrorCode.INVALID_FIELD_FOR_SORTING, field);
//            }
//            if(!SortOrder.isSortOrderExisted(sortType)){
//                throw new SortException(ServiceErrorCode.INVALID_SORT_ORDER, sortType);
//            }
            sortingList.add(new Sorting(field, SortOrder.valueOf(sortType.toUpperCase())));
        }
        return sortingList;
    }

    protected boolean isFieldValid(String field){
        return validFieldsForSorting.stream().anyMatch(validField -> validField.equalsIgnoreCase(field));
    }
}
