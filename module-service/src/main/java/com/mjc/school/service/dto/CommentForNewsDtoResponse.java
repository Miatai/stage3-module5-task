package com.mjc.school.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentForNewsDtoResponse extends RepresentationModel<CommentForNewsDtoResponse> {
    private Long id;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime lastUpdatedDate;
}
