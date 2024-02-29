package com.mjc.school.service.dto;

import com.mjc.school.service.validator.constraint.Max;
import com.mjc.school.service.validator.constraint.Min;
import com.mjc.school.service.validator.constraint.NotNull;
import com.mjc.school.service.validator.constraint.Size;

public record CommentDtoRequest(
    @NotNull
    @Size(min = 5, max = 255)
    String content,

    @NotNull
    Long newsId
) {
}
