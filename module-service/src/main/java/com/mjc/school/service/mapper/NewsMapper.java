package com.mjc.school.service.mapper;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.CommentRepository;
import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.service.dto.CommentForNewsDtoResponse;
import com.mjc.school.service.dto.NewsDtoCreateRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.dto.NewsDtoUpdateRequest;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, TagMapper.class, CommentMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, imports = java.util.stream.Collectors.class)
public abstract class NewsMapper {
    @Autowired
    protected AuthorRepository authorRepository;
    @Autowired
    protected TagRepository tagRepository;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected CommentMapper commentMapper;

    public abstract List<NewsDtoResponse> modelListToDtoList(List<News> modelList);

    @Mapping(source = "author", target = "authorDto")
    @Mapping(source = "tags", target = "tagDtos")
    @Mapping(target = "commentForNewsDtos", ignore = true)
    public abstract NewsDtoResponse modelToDto(News model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastUpdatedDate", ignore = true)
    @Mapping(target = "author", expression =
        "java(authorRepository.readByName(dto.authorName()).get())")
    @Mapping(target = "tags", expression =
        "java(dto.tagsNames().stream().map(name -> tagRepository.readByName(name).get()).collect(Collectors.toList()))")
    @Mapping(target = "comments", expression =
        "java(dto.commentsIds().stream().map(commentId -> commentRepository.getReference(commentId)).collect(Collectors.toList()))")
    public abstract News dtoToModel(NewsDtoCreateRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastUpdatedDate", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "tags", expression =
        "java(dto.tagsNames().stream().map(name -> tagRepository.readByName(name).get()).collect(Collectors.toList()))")
    @Mapping(target = "comments", expression =
        "java(dto.commentsIds().stream().map(commentId -> commentRepository.getReference(commentId)).collect(Collectors.toList()))")
    public abstract News dtoToModel(NewsDtoUpdateRequest dto);

    @AfterMapping
    void setAuthor(NewsDtoUpdateRequest updateRequest, @MappingTarget News news) {
        if (updateRequest.authorName() != null && !updateRequest.authorName().isBlank()) {
            Author author = authorRepository.readByName(updateRequest.authorName()).get();
            news.setAuthor(author);
        }
    }

    @AfterMapping
    void setComments(News model, @MappingTarget NewsDtoResponse dto) {
        if (model.getComments() != null) {
            List<CommentForNewsDtoResponse> commentsDto = model.getComments().stream()
                .map(c -> commentMapper.modelToDtoForNews(c))
                .collect(Collectors.toList());
            dto.setCommentForNewsDtos(commentsDto);
        }
    }
}

