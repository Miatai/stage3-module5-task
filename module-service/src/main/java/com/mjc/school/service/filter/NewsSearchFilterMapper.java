package com.mjc.school.service.filter;

import com.mjc.school.repository.filter.SearchCriteria;
import com.mjc.school.service.dto.SearchFilterDtoRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsSearchFilterMapper extends BaseSearchFilterMapper<SearchFilterDtoRequest> {
    public NewsSearchFilterMapper() {
        validFilterFields = List.of("title", "content", "tags.name", "tags.id", "author.name");
    }

    @Override
    public List<SearchCriteria> map(SearchFilterDtoRequest searchFilterRequest) {
        return createSearchCriteria(searchFilterRequest.getFilters());
    }
}