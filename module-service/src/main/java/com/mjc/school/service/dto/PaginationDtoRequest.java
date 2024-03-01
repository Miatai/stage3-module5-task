package com.mjc.school.service.dto;

import com.mjc.school.service.validator.constraint.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
public class PaginationDtoRequest {
    @Min(1)
    private int page = 1;
    @Min(1)
    private int pageSize = 10;

    public PaginationDtoRequest(@Nullable int page,
                                @Nullable int pageSize) {
        if (page > 0) {
            this.page = page;
        }
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
    }
}
