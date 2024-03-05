package com.mjc.school.service.impl;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.CommentRepository;
import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.exception.EntityConflictRepositoryException;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ResourceConflictServiceException;
import com.mjc.school.service.filter.NewsSearchFilterMapper;
import com.mjc.school.service.mapper.AuthorMapper;
import com.mjc.school.service.mapper.CommentMapper;
import com.mjc.school.service.mapper.NewsMapper;
import com.mjc.school.service.mapper.TagMapper;
import com.mjc.school.service.sort.NewsSortingMapper;
import com.tngtech.archunit.thirdparty.com.google.common.base.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {
    @InjectMocks
    private NewsServiceImpl newsService;
    @Mock
    private NewsRepository newsRepository;
    @Mock
    private NewsSortingMapper newsSortingMapper;
    @Mock
    private NewsSearchFilterMapper newsSearchFilterMapper;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private CommentRepository commentRepository;
    @Spy
    @InjectMocks
    private NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);
    @Spy
    private AuthorMapper authorMapper = Mappers.getMapper(AuthorMapper.class);
    @Spy
    private TagMapper tagMapper = Mappers.getMapper(TagMapper.class);
    @Spy
    private CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);



    @Test
    void readAll_Test() {
        List<News> modelList = List.of(
            new News(1L, "testTitle1", "testContent1", LocalDateTime.now(), LocalDateTime.now(), null, new ArrayList<>(), new ArrayList<>()),
            new News(2L, "testTitle2", "testContent2", LocalDateTime.now(), LocalDateTime.now(), null, new ArrayList<>(), new ArrayList<>())
        );
        Page<News> page = new Page<>(modelList, 1, 1);
        Mockito.when(newsRepository.readAll(any(Pagination.class), any(), any())).thenReturn(page);

        PageDtoResponse<NewsDtoResponse> pageDtoResponse = newsService.readAll(
            new PaginationDtoRequest(1, 5), null, null);

        Assertions.assertEquals(page.entities().size(), pageDtoResponse.getModelDtoList().size());
        Assertions.assertEquals(page.entities().get(0).getContent(), pageDtoResponse.getModelDtoList().get(0).getContent());
        Assertions.assertEquals(page.entities().get(1).getContent(), pageDtoResponse.getModelDtoList().get(1).getContent());
    }

    @Test
    void readById_withValidId_Test() {
        long validId = 5L;
        String validContent = "testContent";
        String validTitle = "testTitle";
        News model = new News(validId,validTitle, validContent, LocalDateTime.now(), LocalDateTime.now(), null, new ArrayList<>(), new ArrayList<>());
        Optional<News> modeloptional = Optional.of(model);
        Mockito.when(newsRepository.readById(validId)).thenReturn(modeloptional.toJavaUtil());
        NewsDtoResponse dtoResponse = newsService.readById(validId);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validContent, dtoResponse.getContent());
        Assertions.assertEquals(validTitle, dtoResponse.getTitle());
    }

    @Test
    void readById_withInvalidId_throwNotFoundException_Test() {
        long invalidId = 999L;
        Assertions.assertThrows(NotFoundException.class, () -> newsService.readById(invalidId));
    }

    @Test
    void create_withValidData_Test() {
        long validId = 5L;
        String validTitle = "testTitle";
        String validContent = "testContent";
        Optional<Author> modelOptional = Optional.of(new Author(1L, "authorName", LocalDateTime.now(), LocalDateTime.now()));
        NewsDtoCreateRequest dtoRequest = new NewsDtoCreateRequest(validId, validTitle, validContent, null, "authorName", null);
        News model = new News(validId, validTitle, validContent, LocalDateTime.now(), LocalDateTime.now(), new Author(1L, "authorName", LocalDateTime.now(), LocalDateTime.now()), new ArrayList<>(), new ArrayList<>());
        Mockito.when(newsRepository.create(any())).thenReturn(model);
        Mockito.when(authorRepository.readByName(any())).thenReturn(modelOptional.toJavaUtil());
        NewsDtoResponse dtoResponse = newsService.create(dtoRequest);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validContent, dtoResponse.getContent());
        Assertions.assertEquals(validTitle, dtoResponse.getTitle());
    }

    @Test
    void create_throwResourceConflictServiceException_Test() {
        long validId = 5L;
        String validTitle = "testTitle";
        String validContent = "testContent";
        Optional<Author> modelOptional = Optional.of(new Author(1L, "authorName", LocalDateTime.now(), LocalDateTime.now()));
        Mockito.when(newsRepository.create(any())).thenThrow(new EntityConflictRepositoryException(""));
        Mockito.when(authorRepository.readByName(any())).thenReturn(modelOptional.toJavaUtil());
        Assertions.assertThrows(ResourceConflictServiceException.class, () -> newsService.create(
            new NewsDtoCreateRequest(validId, validTitle, validContent, null, null, null)));
    }

    @Test
    void update_withValidData_Test() {
        long validId = 5L;
        String validTitle = "testTitle";
        String validContent = "testContent";
        NewsDtoUpdateRequest dtoRequest = new NewsDtoUpdateRequest(validTitle, validContent,"authorName", new ArrayList<>(), new ArrayList<>());
        News model = new News(validId, validTitle, validContent, LocalDateTime.now(), LocalDateTime.now(), null, new ArrayList<>(), new ArrayList<>());
        Mockito.when(newsRepository.update(any())).thenReturn(model);
        Mockito.when(newsRepository.existById(validId)).thenReturn(true);
        NewsDtoResponse dtoResponse = newsService.update(validId, dtoRequest);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validContent, dtoResponse.getContent());
        Assertions.assertEquals(validTitle, dtoResponse.getTitle());
    }

    @Test
    void update_withInvalidId_throwNotFoundException_Test() {
        long invalidId = 999L;
        String validTitle = "testTitle";
        String validContent = "testContent";
        NewsDtoUpdateRequest dtoRequest = new NewsDtoUpdateRequest(validTitle, validContent,"authorName", new ArrayList<>(), new ArrayList<>());
        Assertions.assertThrows(NotFoundException.class, () -> newsService.update(invalidId, dtoRequest));
    }

    @Test
    void deleteById_withValidId_Test() {
        long validId = 1L;
        Mockito.when(newsRepository.existById(validId)).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> newsService.deleteById(validId));
    }

    @Test
    void deleteById_withInvalidId_Test() {
        long invalidId = 999L;
        Assertions.assertThrows(NotFoundException.class, () -> newsService.deleteById(invalidId));
    }
}
