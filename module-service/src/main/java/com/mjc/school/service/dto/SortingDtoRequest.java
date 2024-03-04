package com.mjc.school.service.dto;

import com.mjc.school.service.validator.constraint.SortFieldsAndOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class SortingDtoRequest {
    @SortFieldsAndOrder
    private List<String> sortByAndOrder = new ArrayList<>();

    public SortingDtoRequest(@Nullable List<String> sortByAndOrder) {
        if (sortByAndOrder != null) {
            this.sortByAndOrder = sortByAndOrder;
        }
    }
}
