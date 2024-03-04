package com.mjc.school.service.impl;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.exception.EntityConflictRepositoryException;
import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.NewsService;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ResourceConflictServiceException;
import com.mjc.school.service.filter.NewsSearchFilterMapper;
import com.mjc.school.service.sort.NewsSortingMapper;
import com.mjc.school.service.mapper.NewsMapper;
import com.mjc.school.service.validator.Valid;
import com.mjc.school.service.validator.ValidFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mjc.school.service.exceptions.ServiceErrorCode.NEWS_CONFLICT;
import static com.mjc.school.service.exceptions.ServiceErrorCode.NEWS_ID_DOES_NOT_EXIST;

@Service
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final NewsMapper mapper;

    private final NewsSortingMapper newsSortingMapper;
    private final NewsSearchFilterMapper newsFilterMapper;

    @Autowired
    public NewsServiceImpl(final NewsRepository newsRepository,
                           final AuthorRepository authorRepository,
                           final TagRepository tagRepository,
                           final NewsMapper mapper,
                           final NewsSortingMapper newsSortingMapper,
                           final NewsSearchFilterMapper newsFilterMapper) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.newsSortingMapper = newsSortingMapper;
        this.newsFilterMapper = newsFilterMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public PageDtoResponse<NewsDtoResponse> readAll(@Valid
                                                    PaginationDtoRequest paginationDtoRequest,
                                                    @ValidFields(fields = {"createdDate", "lastUpdatedDate"})
                                                    SortingDtoRequest sortingDtoRequest,
                                                    @ValidFields(fields = {"title", "content", "tags.name", "tags.id", "author.name"})
                                                    SearchFilterDtoRequest searchFilterDtoRequest) {
        Page<News> modelPage = newsRepository.readAll(new Pagination(paginationDtoRequest.getPage(), paginationDtoRequest.getPageSize()),
            newsSortingMapper.map(sortingDtoRequest),
            newsFilterMapper.map(searchFilterDtoRequest));
        List<NewsDtoResponse> responseDtoList = mapper.modelListToDtoList(modelPage.entities());
        return new PageDtoResponse<>(responseDtoList, modelPage.currentPage(), modelPage.pageCount());
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDtoResponse readById(final Long id) {
        return newsRepository
            .readById(id)
            .map(mapper::modelToDto)
            .orElseThrow(
                () -> new NotFoundException(NEWS_ID_DOES_NOT_EXIST, new String[]{id.toString()}));
    }

    @Override
    @Transactional
    public NewsDtoResponse create(@Valid NewsDtoCreateRequest createRequest) {
        createNonExistentAuthor(createRequest.authorName());
        createNonExistentTags(createRequest.tagsNames());
        try {
            News model = mapper.dtoToModel(createRequest);
            model = newsRepository.create(model);
            return mapper.modelToDto(model);
        } catch (EntityConflictRepositoryException e) {
            throw new ResourceConflictServiceException(NEWS_CONFLICT, new String[]{e.getMessage()});
        }
    }

    @Override
    @Transactional
    public NewsDtoResponse update(Long id, @Valid NewsDtoUpdateRequest updateRequest) {
        if (!newsRepository.existById(id)) {
            throw new NotFoundException(NEWS_ID_DOES_NOT_EXIST, new String[]{id.toString()});
        }
        createNonExistentAuthor(updateRequest.authorName());
        createNonExistentTags(updateRequest.tagsNames());

        News model = mapper.dtoToModel(updateRequest);
        model.setId(id);
        model = newsRepository.update(model);
        return mapper.modelToDto(model);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (newsRepository.existById(id)) {
            newsRepository.deleteById(id);
        } else {
            throw new NotFoundException(NEWS_ID_DOES_NOT_EXIST, new String[]{id.toString()});
        }
    }

    private void createNonExistentAuthor(String authorName) {
        if (authorName != null && !authorName.isBlank()) {
            if (authorRepository.readByName(authorName).isEmpty()) {
                Author author = new Author();
                author.setName(authorName);
                authorRepository.create(author);
            }
        }
    }

    private void createNonExistentTags(List<String> tagNames) {
        tagNames.stream()
            .filter(name -> tagRepository.readByName(name).isEmpty())
            .map(name -> {
                Tag tag = new Tag();
                tag.setName(name);
                return tag;
            })
            .forEach(tagRepository::create);
    }
}
