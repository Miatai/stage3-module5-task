package com.mjc.school.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDtoResponse extends RepresentationModel<AuthorDtoResponse> {
    private Long id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime lastUpdatedDate;
}
