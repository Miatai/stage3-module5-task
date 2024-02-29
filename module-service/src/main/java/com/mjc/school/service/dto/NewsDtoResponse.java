package com.mjc.school.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDtoResponse extends RepresentationModel<NewsDtoResponse> {
    private Long id;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime lastUpdatedDate;
    private AuthorDtoResponse authorDto;
    private List<TagDtoResponse> tagDtos;
    private List<CommentForNewsDtoResponse> commentForNewsDtos;
}
