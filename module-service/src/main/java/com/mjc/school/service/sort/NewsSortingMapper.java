package com.mjc.school.service.sort;

import com.mjc.school.repository.sorting.SortOrder;
import com.mjc.school.repository.sorting.Sorting;
import com.mjc.school.service.dto.SortingDtoRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsSortingMapper extends BaseSortingMapper<SortingDtoRequest> {
    private static final String CREATED_DATE_FIELD = "createdDate";
    private static final SortOrder CREATED_FIELD_SORT_TYPE = SortOrder.DESC;

    @Override
    public List<Sorting> map(SortingDtoRequest sortingRequest) {
        DEFAULT_SORTING_MAP.put(CREATED_DATE_FIELD, CREATED_FIELD_SORT_TYPE);
        return createSorting(sortingRequest.getSortByAndOrder());
    }
}
