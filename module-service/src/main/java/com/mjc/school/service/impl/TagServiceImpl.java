package com.mjc.school.service.impl;

import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.exception.EntityConflictRepositoryException;
import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.TagService;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ResourceConflictServiceException;
import com.mjc.school.service.mapper.TagMapper;
import com.mjc.school.service.validator.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mjc.school.service.exceptions.ServiceErrorCode.TAG_CONFLICT;
import static com.mjc.school.service.exceptions.ServiceErrorCode.TAG_ID_DOES_NOT_EXIST;

@Service
public class TagServiceImpl implements TagService {
    private final TagMapper mapper;
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(
        final TagRepository tagRepository,
        final TagMapper mapper) {
        this.tagRepository = tagRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PageDtoResponse<TagDtoResponse> readAll(@Valid PaginationDtoRequest paginationDtoRequest,
                                                   SortingDtoRequest sortingDtoRequest,
                                                   SearchFilterDtoRequest searchFilterDtoRequest) {

        Page<Tag> modelPage = tagRepository.readAll(new Pagination(paginationDtoRequest.getPage(), paginationDtoRequest.getPageSize()),
            null,
            null);
        List<TagDtoResponse> responseDtoList = mapper.modelListToDtoList(modelPage.entities());
        return new PageDtoResponse<>(responseDtoList, modelPage.currentPage(), modelPage.pageCount());
    }

    @Override
    @Transactional(readOnly = true)
    public TagDtoResponse readById(final Long id) {
        return tagRepository
            .readById(id)
            .map(mapper::modelToDto)
            .orElseThrow(
                () -> new NotFoundException(TAG_ID_DOES_NOT_EXIST, id.toString()));
    }

    @Override
    @Transactional
    public TagDtoResponse create(@Valid TagDtoRequest createRequest) {
        try {
            Tag model = mapper.dtoToModel(createRequest);
            model = tagRepository.create(model);
            return mapper.modelToDto(model);
        } catch (EntityConflictRepositoryException e) {
            throw new ResourceConflictServiceException(TAG_CONFLICT, e.getMessage());
        }
    }

    @Override
    @Transactional
    public TagDtoResponse update(Long id, @Valid TagDtoRequest updateRequest) {
        if (tagRepository.existById(id)) {
            Tag model = mapper.dtoToModel(updateRequest);
            model = tagRepository.update(model);
            return mapper.modelToDto(model);
        } else {
            throw new NotFoundException(TAG_ID_DOES_NOT_EXIST, id.toString());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (tagRepository.existById(id)) {
            tagRepository.deleteById(id);
        } else {
            throw new NotFoundException(TAG_ID_DOES_NOT_EXIST, id.toString());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageDtoResponse<TagDtoResponse> readByNewsId(Long newsId,
                                                        @Valid PaginationDtoRequest paginationDtoRequest,
                                                        SortingDtoRequest sortingDtoRequest) {
        Page<Tag> modelPage = tagRepository.readByNewsId(newsId,
            new Pagination(paginationDtoRequest.getPage(), paginationDtoRequest.getPageSize()),
            null);
        List<TagDtoResponse> responseDtoList = mapper.modelListToDtoList(modelPage.entities());
        return new PageDtoResponse<>(responseDtoList, modelPage.currentPage(), modelPage.pageCount());
    }
}
