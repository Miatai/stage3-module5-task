package com.mjc.school.service.impl;

import com.mjc.school.repository.CommentRepository;
import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.exception.EntityConflictRepositoryException;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ResourceConflictServiceException;
import com.mjc.school.service.mapper.CommentMapper;
import com.mjc.school.service.sort.CommentSortingMapper;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private CommentSortingMapper commentSortingMapper;

    @Spy
    @InjectMocks
    private CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @Test
    void readAll_Test(){
        List<Comment> modelList = List.of(
            new Comment(1L, "testComment1", LocalDateTime.now(), LocalDateTime.now(), null),
            new Comment(5L, "testComment2", LocalDateTime.now(), LocalDateTime.now(), null)
        );
        Page<Comment> page = new Page<>(modelList, 1, 1);
        Mockito.when(commentRepository.readAll(any(Pagination.class), any(), any())).thenReturn(page);

        PageDtoResponse<CommentDtoResponse> pageDtoResponse = commentService.readAll(
            new PaginationDtoRequest(1, 5), null, null);

        Assertions.assertEquals(page.entities().size(), pageDtoResponse.getModelDtoList().size());
        Assertions.assertEquals(page.entities().get(0).getContent(), pageDtoResponse.getModelDtoList().get(0).getContent());
        Assertions.assertEquals(page.entities().get(1).getContent(), pageDtoResponse.getModelDtoList().get(1).getContent());
    }

    @Test
    void readById_withValidId_Test(){
        long validId = 5L;
        String validName = "testComment";
        Comment model = new Comment(validId, validName, LocalDateTime.now(), LocalDateTime.now(),null);
        Optional<Comment> modeloptional = Optional.of(model);
        Mockito.when(commentRepository.readById(validId)).thenReturn(modeloptional.toJavaUtil());
        CommentDtoResponse dtoResponse = commentService.readById(validId);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validName, dtoResponse.getContent());
    }

    @Test
    void readById_withInvalidId_throwNotFoundException_Test(){
        long invalidId = 999L;
        Assertions.assertThrows(NotFoundException.class, () -> commentService.readById(invalidId));
    }

    @Test
    void create_withValidData_Test(){
        long validId = 5L;
        String validName = "testComment";
        CommentDtoRequest dtoRequest = new CommentDtoRequest(validName, null);
        Comment model = new Comment(validId, validName, LocalDateTime.now(), LocalDateTime.now(), null);
        Mockito.when(commentRepository.create(any())).thenReturn(model);
        Mockito.when(newsRepository.existById(any())).thenReturn(true);
        Mockito.when(newsRepository.getReference(any())).thenReturn(new News());
        CommentDtoResponse dtoResponse = commentService.create(dtoRequest);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validName, dtoResponse.getContent());
    }

    @Test
    void create_throwResourceConflictServiceException_Test(){
        Mockito.when(commentRepository.create(any())).thenThrow(new EntityConflictRepositoryException(""));
        Mockito.when(newsRepository.existById(any())).thenReturn(true);
        Assertions.assertThrows(ResourceConflictServiceException.class, () -> commentService.create(new CommentDtoRequest("someName",
            1L)));
    }

    @Test
    void update_withValidData_Test(){
        long validId = 5L;
        String validName = "testComment";
        CommentDtoRequest dtoRequest = new CommentDtoRequest(validName, null);
        Comment model = new Comment(validId, validName, LocalDateTime.now(),LocalDateTime.now(),null);
        Mockito.when(commentRepository.existById(validId)).thenReturn(true);
        Mockito.when(commentRepository.update(any())).thenReturn(model);
        CommentDtoResponse dtoResponse = commentService.update(validId, dtoRequest);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validName, dtoResponse.getContent());
    }

    @Test
    void update_withInvalidId_throwNotFoundException_Test(){
        long invalidId = 999L;
        String validName = "testComment";
        CommentDtoRequest dtoRequest = new CommentDtoRequest(validName,null);
        Assertions.assertThrows(NotFoundException.class, () -> commentService.update(invalidId, dtoRequest));
    }

    @Test
    void deleteById_withValidId_Test(){
        long validId = 1L;
        Mockito.when(commentRepository.existById(validId)).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> commentService.deleteById(validId));
    }

    @Test
    void deleteById_withInvalidId_Test(){
        long invalidId = 999L;
        Assertions.assertThrows(NotFoundException.class, () -> commentService.deleteById(invalidId));
    }

    @Test
    void readByNewsId_withValidId_Test() {
        long validId = 1L;
        List<Comment> modelList = List.of(
            new Comment(1L, "testComment1", LocalDateTime.now(), LocalDateTime.now(), null),
            new Comment(5L, "testComment2", LocalDateTime.now(),LocalDateTime.now(),null)
        );
        Page<Comment> page = new Page<>(modelList, 1, 1);
        Mockito.when(commentRepository.readByNewsId(eq(validId), any(Pagination.class), any())).thenReturn(page);
        Mockito.when(newsRepository.existById(validId)).thenReturn(true);
        PageDtoResponse<CommentForNewsDtoResponse> pageDtoResponse = commentService.readByNewsId(validId, new PaginationDtoRequest(1, 5), null);
        Assertions.assertEquals(page.entities().size(), pageDtoResponse.getModelDtoList().size());
        Assertions.assertEquals(page.entities().get(0).getContent(), pageDtoResponse.getModelDtoList().get(0).getContent());
        Assertions.assertEquals(page.entities().get(1).getContent(), pageDtoResponse.getModelDtoList().get(1).getContent());
    }

    @Test
    void readByNewsId_withInvalidId_throwNotFoundException_Test() {
        long validId = 1L;
        List<Comment> modelList = List.of(
            new Comment(1L, "testComment1", LocalDateTime.now(), LocalDateTime.now(), null),
            new Comment(5L, "testComment2", LocalDateTime.now(), LocalDateTime.now(), null)
        );
        Page<Comment> page = new Page<>(modelList, 1, 1);
        Mockito.when(newsRepository.existById(validId)).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> commentService.readByNewsId(validId, new PaginationDtoRequest(1, 5), null));
    }

}
