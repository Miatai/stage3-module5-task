package com.mjc.school.service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mjc.school.service.validator.constraint.Min;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
public class PaginationDtoRequest {
    @Min(1)
    @Schema(description = "Page number", example = "1", required = false, defaultValue = "1")
    private int page = 1;
    @Min(1)
    @Schema(description = "Page size", example = "10", required = false, defaultValue = "10")
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
