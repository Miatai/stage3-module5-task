package com.mjc.school.service.dto;

import com.mjc.school.service.validator.constraint.Max;
import com.mjc.school.service.validator.constraint.Min;
import com.mjc.school.service.validator.constraint.NotNull;
import com.mjc.school.service.validator.constraint.Size;

import java.util.ArrayList;
import java.util.List;

public record NewsDtoCreateRequest(
    @Min(1)
    @Max(Long.MAX_VALUE)
    Long id,

    @NotNull
    @Size(min = 5, max = 30)
    String title,

    @NotNull
    @Size(min = 5, max = 255)
    String content,

    List<Long> commentsIds,

    @Size(min = 3, max = 15)
    String authorName,

    @Size(min = 3, max = 15)
    List<String> tagsNames
) {
    public NewsDtoCreateRequest {
        if (tagsNames == null) {
            tagsNames = new ArrayList<>();
        }
        if (commentsIds == null) {
            commentsIds = new ArrayList<>();
        }
    }
}
