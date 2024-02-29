package com.mjc.school.service.mapper;

import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.service.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class CommentMapper {
    @Autowired
    protected NewsRepository newsRepository;

    public abstract List<CommentDtoResponse> modelListToDtoList(List<Comment> modelList);

    public abstract List<CommentForNewsDtoResponse> modelListToForNewsDtoList(List<Comment> modelList);

    @Mapping(source = "news", target = "newsDto")
    public abstract CommentDtoResponse modelToDto(Comment model);

    public abstract CommentForNewsDtoResponse modelToDtoForNews(Comment model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastUpdatedDate", ignore = true)
    @Mapping(target = "news", expression = "java(newsRepository.getReference(dto.newsId()))")
    public abstract Comment dtoToModel(CommentDtoRequest dto);
}