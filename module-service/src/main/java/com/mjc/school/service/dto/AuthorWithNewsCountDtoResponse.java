package com.mjc.school.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AuthorWithNewsCountDtoResponse extends RepresentationModel<AuthorWithNewsCountDtoResponse> {
    private Long id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime lastUpdatedDate;
    private Long newsCount;
}
