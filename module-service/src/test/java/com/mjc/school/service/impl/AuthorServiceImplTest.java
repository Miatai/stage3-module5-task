package com.mjc.school.service.impl;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.exception.EntityConflictRepositoryException;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.repository.query.AuthorWithNewsCount;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ResourceConflictServiceException;
import com.mjc.school.service.mapper.AuthorMapper;
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

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorRepository authorRepository;

    @Spy
    private AuthorMapper authorMapper = Mappers.getMapper(AuthorMapper.class);

    @Test
    void readAll_Test(){
        List<Author>modelList = List.of(
            new Author(1L, "testAuthor1", LocalDateTime.now(), LocalDateTime.now()),
            new Author(5L, "testAuthor2", LocalDateTime.now(), LocalDateTime.now())
        );
        Page<Author> page = new Page<>(modelList, 1, 1);
        Mockito.when(authorRepository.readAll(any(Pagination.class), any(), any())).thenReturn(page);

        PageDtoResponse<AuthorDtoResponse> pageDtoResponse = authorService.readAll(
            new PaginationDtoRequest(1, 5), null, null);

        Assertions.assertEquals(page.entities().size(), pageDtoResponse.getModelDtoList().size());
        Assertions.assertEquals(page.entities().get(0).getName(), pageDtoResponse.getModelDtoList().get(0).getName());
        Assertions.assertEquals(page.entities().get(1).getName(), pageDtoResponse.getModelDtoList().get(1).getName());
    }

    @Test
    void readById_withValidId_Test(){
        long validId = 5L;
        String validName = "testAuthor";
        Author model = new Author(validId, validName, LocalDateTime.now(), LocalDateTime.now());
        Optional<Author> modeloptional = Optional.of(model);
        Mockito.when(authorRepository.readById(validId)).thenReturn(modeloptional.toJavaUtil());
        AuthorDtoResponse dtoResponse = authorService.readById(validId);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validName, dtoResponse.getName());
    }

    @Test
    void readById_withInvalidId_throwNotFoundException_Test(){
        long invalidId = 999L;
        Assertions.assertThrows(NotFoundException.class, () -> authorService.readById(invalidId));
    }

    @Test
    void create_withValidData_Test(){
        long validId = 5L;
        String validName = "testAuthor";
        AuthorDtoRequest dtoRequest = new AuthorDtoRequest(validName);
        Author model = new Author(validId, validName, LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(authorRepository.create(authorMapper.dtoToModel(dtoRequest))).thenReturn(model);
        AuthorDtoResponse dtoResponse = authorService.create(dtoRequest);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validName, dtoResponse.getName());
    }

    @Test
    void create_throwResourceConflictServiceException_Test(){
        Mockito.when(authorRepository.create(any())).thenThrow(new EntityConflictRepositoryException(""));
        Assertions.assertThrows(ResourceConflictServiceException.class, () -> authorService.create(new AuthorDtoRequest("someName")));
    }

    @Test
    void update_withValidData_Test(){
        long validId = 5L;
        String validName = "testAuthor";
        AuthorDtoRequest dtoRequest = new AuthorDtoRequest(validName);
        Author model = new Author(validId, validName, LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(authorRepository.existById(validId)).thenReturn(true);
        Mockito.when(authorRepository.update(authorMapper.dtoToModel(dtoRequest))).thenReturn(model);
        AuthorDtoResponse dtoResponse = authorService.update(validId, dtoRequest);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validName, dtoResponse.getName());
    }

    @Test
    void update_withInvalidId_throwNotFoundException_Test(){
        long invalidId = 999L;
        String validName = "testAuthor";
        AuthorDtoRequest dtoRequest = new AuthorDtoRequest(validName);
//        Mockito.when(authorRepository.existById(invalidId)).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> authorService.update(invalidId, dtoRequest));
    }

    @Test
    void deleteById_withValidId_Test(){
        long validId = 1L;
        Mockito.when(authorRepository.existById(validId)).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> authorService.deleteById(validId));
    }

    @Test
    void deleteById_withInvalidId_Test(){
        long invalidId = 999L;
//        Mockito.when(authorRepository.existById(invalidId)).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> authorService.deleteById(invalidId));
    }

    @Test
    void readByNewsId_withValidId_Test(){
        long validId = 1L;
        String validName = "testAuthor";
        Author model = new Author(validId, validName, LocalDateTime.now(), LocalDateTime.now());
        Optional<Author> modeloptional = Optional.of(model);
        Mockito.when(authorRepository.readByNewsId(validId)).thenReturn(modeloptional.toJavaUtil());
        AuthorDtoResponse dtoResponse = authorService.readByNewsId(validId);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validName, dtoResponse.getName());
    }

    @Test
    void readByNewsId_withInvalidId_throwNotFoundException_Test(){
        long invalidId = 999L;
        Assertions.assertThrows(NotFoundException.class, () -> authorService.readByNewsId(invalidId));
    }

    @Test
    void readWithNewsCount_Test(){
        List<AuthorWithNewsCount>modelList = List.of(
            new AuthorWithNewsCount(new Author(1L, "testAuthor1", LocalDateTime.now(), LocalDateTime.now()), 5L),
            new AuthorWithNewsCount(new Author(5L, "testAuthor2", LocalDateTime.now(), LocalDateTime.now()), 8L)
        );
        Page<AuthorWithNewsCount> page = new Page<>(modelList, 1, 1);
        Mockito.when(authorRepository.readWithNewsCount(any(Pagination.class))).thenReturn(page);

        PageDtoResponse<AuthorWithNewsCountDtoResponse> pageDtoResponse = authorService.readWithNewsCount(new PaginationDtoRequest(1, 5));

        Assertions.assertEquals(page.entities().size(), pageDtoResponse.getModelDtoList().size());
        Assertions.assertEquals(page.entities().get(0).getNewsCount(), pageDtoResponse.getModelDtoList().get(0).getNewsCount());
        Assertions.assertEquals(page.entities().get(1).getNewsCount(), pageDtoResponse.getModelDtoList().get(1).getNewsCount());
        Assertions.assertEquals(page.entities().get(0).getAuthor().getName(), pageDtoResponse.getModelDtoList().get(0).getName());
        Assertions.assertEquals(page.entities().get(1).getAuthor().getName(), pageDtoResponse.getModelDtoList().get(1).getName());
    }
}
