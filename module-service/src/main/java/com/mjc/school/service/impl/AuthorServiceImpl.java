package com.mjc.school.service.impl;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.exception.EntityConflictRepositoryException;
import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.query.AuthorWithNewsCount;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ResourceConflictServiceException;
import com.mjc.school.service.mapper.AuthorMapper;
import com.mjc.school.service.validator.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.mjc.school.service.exceptions.ServiceErrorCode.*;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper mapper;


    @Autowired
    public AuthorServiceImpl(final AuthorRepository authorRepository,
                             final AuthorMapper mapper) {
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PageDtoResponse<AuthorDtoResponse> readAll(@Valid PaginationDtoRequest paginationDtoRequest,
                                                      SortingDtoRequest sortingDtoRequest,
                                                      SearchFilterDtoRequest searchFilterDtoRequest) {
        Page<Author> modelPage = authorRepository.readAll(new Pagination(paginationDtoRequest.getPage(), paginationDtoRequest.getPageSize()),
            null,
            null);
        List<AuthorDtoResponse> responseDtoList = mapper.modelListToDtoList(modelPage.entities());
        return new PageDtoResponse<>(responseDtoList, modelPage.currentPage(), modelPage.pageCount());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDtoResponse readById(Long id) {
        return authorRepository
            .readById(id)
            .map(mapper::modelToDto)
            .orElseThrow(
                () -> new NotFoundException(AUTHOR_ID_DOES_NOT_EXIST, id.toString()));
    }

    @Override
    @Transactional
    public AuthorDtoResponse create(@Valid AuthorDtoRequest createRequest) {
        try {
            Author model = mapper.dtoToModel(createRequest);
            model = authorRepository.create(model);
            return mapper.modelToDto(model);
        } catch (EntityConflictRepositoryException e) {
            throw new ResourceConflictServiceException(AUTHOR_CONFLICT, e.getMessage());
        }
    }

    @Override
    @Transactional
    public AuthorDtoResponse update(Long id, @Valid AuthorDtoRequest updateRequest) {
        if (authorRepository.existById(id)) {
            Author model = mapper.dtoToModel(updateRequest);
            model = authorRepository.update(model);
            return mapper.modelToDto(model);
        } else {
            throw new NotFoundException(AUTHOR_ID_DOES_NOT_EXIST, id.toString());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (authorRepository.existById(id)) {
            authorRepository.deleteById(id);
        } else {
            throw new NotFoundException(AUTHOR_ID_DOES_NOT_EXIST,  id.toString());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDtoResponse readByNewsId(Long newsId) {
        return authorRepository.readByNewsId(newsId)
            .map(mapper::modelToDto)
            .orElseThrow(
                () -> new NotFoundException(AUTHOR_DOES_NOT_EXIST_FOR_NEWS_ID, newsId.toString()));
    }

    @Override
    public PageDtoResponse<AuthorWithNewsCountDtoResponse> readWithNewsCount(@Valid PaginationDtoRequest paginationDtoRequest,
                                                                             SortingDtoRequest sortingDtoRequest) {
        Page<AuthorWithNewsCount> modelPage = authorRepository.
            readWithNewsCount(new Pagination(paginationDtoRequest.getPage(), paginationDtoRequest.getPageSize()));
        List<AuthorWithNewsCountDtoResponse> dtoResponses = modelPage.entities().stream()
            .map(a -> new AuthorWithNewsCountDtoResponse(
                a.getAuthor().getId(),
                a.getAuthor().getName(),
                a.getAuthor().getCreatedDate(),
                a.getAuthor().getLastUpdatedDate(),
                a.getNewsCount()))
            .collect(Collectors.toList());
        return new PageDtoResponse<>(dtoResponses, modelPage.currentPage(), modelPage.pageCount());
    }
}
