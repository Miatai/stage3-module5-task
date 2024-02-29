package com.mjc.school.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mjc.school.service.validator.constraint.SortAndOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class SortingDtoRequest {
    @SortAndOrder
    @Schema(description = "Field for sorting and sort type", example = "field:sortType", required = false)
    private List<String> sortByAndOrder = new ArrayList<>();

    public SortingDtoRequest(@Nullable List<String> sortByAndOrder) {
        if (sortByAndOrder != null) {
            this.sortByAndOrder = sortByAndOrder;
        }
    }
}
