package com.mjc.school.service.impl;

import com.mjc.school.repository.CommentRepository;
import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.exception.EntityConflictRepositoryException;
import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.service.CommentService;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ResourceConflictServiceException;
import com.mjc.school.service.sort.CommentSortingMapper;
import com.mjc.school.service.mapper.CommentMapper;
import com.mjc.school.service.validator.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mjc.school.service.exceptions.ServiceErrorCode.*;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final CommentMapper mapper;
    private final CommentSortingMapper commentSortingMapper;

    @Autowired
    public CommentServiceImpl(final CommentRepository commentRepository,
                              final CommentMapper mapper,
                              final NewsRepository newsRepository,
                              final CommentSortingMapper commentSortingMapper) {
        this.commentRepository = commentRepository;
        this.mapper = mapper;
        this.newsRepository = newsRepository;
        this.commentSortingMapper = commentSortingMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public PageDtoResponse<CommentDtoResponse> readAll(@Valid PaginationDtoRequest paginationDtoRequest,
                                                       SortingDtoRequest sortingDtoRequest,
                                                       SearchFilterDtoRequest searchFilterDtoRequest) {
        Page<Comment> modelPage = commentRepository.readAll(new Pagination(paginationDtoRequest.getPage(), paginationDtoRequest.getPageSize()),
            commentSortingMapper.map(sortingDtoRequest),
            null);
        List<CommentDtoResponse> responseDtoList = mapper.modelListToDtoList(modelPage.entities());
        return new PageDtoResponse<>(responseDtoList, modelPage.currentPage(), modelPage.pageCount());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDtoResponse readById(Long id) {
        return commentRepository
            .readById(id)
            .map(mapper::modelToDto)
            .orElseThrow(
                () -> new NotFoundException(COMMENT_ID_DOES_NOT_EXIST,  id.toString()));
    }

    @Override
    @Transactional
    public CommentDtoResponse create(@Valid CommentDtoRequest createRequest) {
        if (!newsRepository.existById(createRequest.newsId())) {
            throw new NotFoundException(NEWS_ID_DOES_NOT_EXIST, createRequest.newsId().toString());
        }
        try {
            Comment model = mapper.dtoToModel(createRequest);
            model = commentRepository.create(model);
            return mapper.modelToDto(model);
        } catch (EntityConflictRepositoryException e) {
            throw new ResourceConflictServiceException(COMMENT_CONFLICT, e.getMessage());
        }
    }

    @Override
    @Transactional
    public CommentDtoResponse update(Long id, @Valid CommentDtoRequest updateRequest) {
        if (commentRepository.existById(id)) {
            Comment model = mapper.dtoToModel(updateRequest);
            model = commentRepository.update(model);
            return mapper.modelToDto(model);
        } else {
            throw new NotFoundException(COMMENT_ID_DOES_NOT_EXIST, id.toString());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (commentRepository.existById(id)) {
            commentRepository.deleteById(id);
        } else {
            throw new NotFoundException(COMMENT_ID_DOES_NOT_EXIST,  id.toString());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageDtoResponse<CommentForNewsDtoResponse> readByNewsId(Long newsId,
                                                                   @Valid PaginationDtoRequest paginationDtoRequest,
                                                                   SortingDtoRequest sortingDtoRequest) {
        Page<Comment> modelPage = commentRepository.readByNewsId(newsId,
            new Pagination(paginationDtoRequest.getPage(), paginationDtoRequest.getPageSize()),
            commentSortingMapper.map(sortingDtoRequest));
        List<CommentForNewsDtoResponse> responseDtoList = mapper.modelListToForNewsDtoList(modelPage.entities());
        return new PageDtoResponse<>(responseDtoList, modelPage.currentPage(), modelPage.pageCount());
    }
}
