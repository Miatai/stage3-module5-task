package com.mjc.school.service.dto;

import com.mjc.school.service.validator.constraint.Max;
import com.mjc.school.service.validator.constraint.Min;
import com.mjc.school.service.validator.constraint.NotNull;
import com.mjc.school.service.validator.constraint.Size;
import lombok.Builder;

@Builder
public record AuthorDtoRequest(
    @NotNull
    @Size(min = 3, max = 15)
    String name) {
}
