package com.mjc.school.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDtoResponse extends RepresentationModel<TagDtoResponse> {
    private Long id;
    private String name;
}
