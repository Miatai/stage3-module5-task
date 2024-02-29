package com.mjc.school.service.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TagMapper {
    List<TagDtoResponse> modelListToDtoList(List<Tag> modelList);

    TagDtoResponse modelToDto(Tag model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "news", ignore = true)
    Tag dtoToModel(TagDtoRequest dto);
}
