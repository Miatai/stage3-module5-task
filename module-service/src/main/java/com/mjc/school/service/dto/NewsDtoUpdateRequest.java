package com.mjc.school.service.dto;

import com.mjc.school.service.validator.constraint.Size;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public record NewsDtoUpdateRequest (
    @Nullable
    @Size(min = 5, max = 30)
    String title,

    @Nullable
    @Size(min = 5, max = 255)
    String content,

    @Nullable
    String authorName,

    @Nullable
    List<String> tagsNames,

    @Nullable
    List<Long> commentsIds
) {
     public NewsDtoUpdateRequest {
            if (tagsNames == null) {
                tagsNames = new ArrayList<>();
            }
            if (commentsIds == null) {
                commentsIds = new ArrayList<>();
            }
        }
}
