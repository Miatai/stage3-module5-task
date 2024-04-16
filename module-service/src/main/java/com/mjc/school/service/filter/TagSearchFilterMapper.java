package com.mjc.school.service.filter;

import com.mjc.school.repository.filter.SearchCriteria;
import com.mjc.school.service.dto.SearchFilterDtoRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagSearchFilterMapper extends BaseSearchFilterMapper<SearchFilterDtoRequest> {

    @Override
    public List<SearchCriteria> map(SearchFilterDtoRequest searchFilterRequest) {
        return createSearchCriteria(searchFilterRequest.getFilters());
    }
}
