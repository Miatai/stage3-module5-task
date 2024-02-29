package com.mjc.school.service.sort;

import com.mjc.school.repository.sorting.Sorting;
import com.mjc.school.service.dto.SortingDtoRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsSortingMapper extends BaseSortingMapper<SortingDtoRequest> {

    public NewsSortingMapper() {
        validFieldsForSorting = List.of("createdDate", "lastUpdatedDate");
    }

    @Override
    public List<Sorting> map(SortingDtoRequest sortingRequest) {
        return createSorting(sortingRequest.getSortByAndOrder());
    }
}
