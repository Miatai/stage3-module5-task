package com.mjc.school.service.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.CommentRepository;
import com.mjc.school.repository.TagRepository;
import com.mjc.school.service.dto.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.BaseService;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, TagMapper.class})
public abstract class NewsMapper {
    @Autowired
    protected AuthorRepository authorRepository;
    @Autowired
    protected TagRepository tagRepository;
    @Autowired
    protected CommentRepository commentRepository;

    public abstract List<NewsDtoResponse> modelListToDtoList(List<News> modelList);

    //    @Mapping(source = "authorName", target = "authorDto")
//    @Mapping(source = "tagsNames", target = "tagDtos")
//    @Mapping(source = "comments", target = "commentForNewsDtos")
//    @Mapping(target = "authorName", ignore = true)
//    @Mapping(target = "tagsNames", ignore = true)
    @Mapping(target = "authorDto", source = "author")
    @Mapping(target = "tagDtos", source = "tags")
    @Mapping(target = "commentForNewsDtos", source = "comments")
    public abstract NewsDtoResponse modelToDto(News model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastUpdatedDate", ignore = true)
    @Mapping(target = "author", expression =
        "java(authorRepository.readByName(dto.authorName()).get())")
    @Mapping(target = "tags", expression =
        "java(dto.tagsNames().stream().map(name -> tagRepository.readByName(name).get()).toList())")
    @Mapping(target = "comments", expression =
        "java(dto.commentsIds().stream().map(commentId -> commentRepository.getReference(commentId)).toList())")
    public abstract News dtoToModel(NewsDtoCreateRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastUpdatedDate", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "tags", expression =
        "java(dto.tagsNames().stream().map(name -> tagRepository.readByName(name).get()).toList())")
    @Mapping(target = "comments", expression =
        "java(dto.commentsIds().stream().map(commentId -> commentRepository.getReference(commentId)).toList())")
    public abstract News dtoToModel(NewsDtoUpdateRequest dto);
}

