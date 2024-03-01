package com.mjc.school.service.dto;

import com.mjc.school.service.validator.constraint.SearchCriteria;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class SearchFilterDtoRequest {
    @SearchCriteria
    @Schema(description = "Field and value for filtering", example = "field:value", required = false)
    private List<String> filters = new ArrayList<>();

    public SearchFilterDtoRequest(@Nullable List<String> filters) {
        if (filters != null) {
            this.filters = filters;
        }
    }
}
