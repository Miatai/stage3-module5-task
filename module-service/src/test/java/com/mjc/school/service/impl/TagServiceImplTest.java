package com.mjc.school.service.impl;

import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.exception.EntityConflictRepositoryException;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ResourceConflictServiceException;
import com.mjc.school.service.mapper.TagMapper;
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
class TagServiceImplTest {
    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private NewsRepository newsRepository;

    @Spy
    private TagMapper tagMapper = Mappers.getMapper(TagMapper.class);

    @Test
    void readAll_Test(){
        List<Tag> modelList = List.of(
            new Tag(1L, "testTag1"),
            new Tag(5L, "testTag2")
        );
        Page<Tag> page = new Page<>(modelList, 1, 1);
        Mockito.when(tagRepository.readAll(any(Pagination.class), any(), any())).thenReturn(page);

        PageDtoResponse<TagDtoResponse> pageDtoResponse = tagService.readAll(
            new PaginationDtoRequest(1, 5), null, null);

        Assertions.assertEquals(page.entities().size(), pageDtoResponse.getModelDtoList().size());
        Assertions.assertEquals(page.entities().get(0).getName(), pageDtoResponse.getModelDtoList().get(0).getName());
        Assertions.assertEquals(page.entities().get(1).getName(), pageDtoResponse.getModelDtoList().get(1).getName());
    }

    @Test
    void readById_withValidId_Test(){
        long validId = 5L;
        String validName = "testTag";
        Tag model = new Tag(validId, validName);
        Optional<Tag> modeloptional = Optional.of(model);
        Mockito.when(tagRepository.readById(validId)).thenReturn(modeloptional.toJavaUtil());
        TagDtoResponse dtoResponse = tagService.readById(validId);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validName, dtoResponse.getName());
    }

    @Test
    void readById_withInvalidId_throwNotFoundException_Test(){
        long invalidId = 999L;
        Assertions.assertThrows(NotFoundException.class, () -> tagService.readById(invalidId));
    }

    @Test
    void create_withValidData_Test(){
        long validId = 5L;
        String validName = "testTag";
        TagDtoRequest dtoRequest = new TagDtoRequest(validName);
        Tag model = new Tag(validId, validName);
        Mockito.when(tagRepository.create(tagMapper.dtoToModel(dtoRequest))).thenReturn(model);
        TagDtoResponse dtoResponse = tagService.create(dtoRequest);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validName, dtoResponse.getName());
    }

    @Test
    void create_throwResourceConflictServiceException_Test(){
        Mockito.when(tagRepository.create(any())).thenThrow(new EntityConflictRepositoryException(""));
        Assertions.assertThrows(ResourceConflictServiceException.class, () -> tagService.create(new TagDtoRequest("someName")));
    }

    @Test
    void update_withValidData_Test(){
        long validId = 5L;
        String validName = "testTag";
        TagDtoRequest dtoRequest = new TagDtoRequest(validName);
        Tag model = new Tag(validId, validName);
        Mockito.when(tagRepository.existById(validId)).thenReturn(true);
        Mockito.when(tagRepository.update(tagMapper.dtoToModel(dtoRequest))).thenReturn(model);
        TagDtoResponse dtoResponse = tagService.update(validId, dtoRequest);
        Assertions.assertEquals(validId, dtoResponse.getId());
        Assertions.assertEquals(validName, dtoResponse.getName());
    }

    @Test
    void update_withInvalidId_throwNotFoundException_Test(){
        long invalidId = 999L;
        String validName = "testTag";
        TagDtoRequest dtoRequest = new TagDtoRequest(validName);
        Assertions.assertThrows(NotFoundException.class, () -> tagService.update(invalidId, dtoRequest));
    }

    @Test
    void deleteById_withValidId_Test(){
        long validId = 1L;
        Mockito.when(tagRepository.existById(validId)).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> tagService.deleteById(validId));
    }

    @Test
    void deleteById_withInvalidId_Test(){
        long invalidId = 999L;
        Assertions.assertThrows(NotFoundException.class, () -> tagService.deleteById(invalidId));
    }

    @Test
    void readByNewsId_withValidId_Test() {
        long validId = 1L;
        List<Tag> modelList = List.of(
            new Tag(1L, "testTag1"),
            new Tag(5L, "testTag2")
        );
        Page<Tag> page = new Page<>(modelList, 1, 1);
        Mockito.when(tagRepository.readByNewsId(eq(validId), any(Pagination.class))).thenReturn(page);
        Mockito.when(newsRepository.existById(validId)).thenReturn(true);
        PageDtoResponse<TagDtoResponse> pageDtoResponse = tagService.readByNewsId(validId, new PaginationDtoRequest(1, 5));
        Assertions.assertEquals(page.entities().size(), pageDtoResponse.getModelDtoList().size());
        Assertions.assertEquals(page.entities().get(0).getName(), pageDtoResponse.getModelDtoList().get(0).getName());
        Assertions.assertEquals(page.entities().get(1).getName(), pageDtoResponse.getModelDtoList().get(1).getName());
    }

    @Test
    void readByNewsId_withInvalidId_throwNotFoundException_Test() {
        long validId = 1L;
        List<Tag> modelList = List.of(
            new Tag(1L, "testTag1"),
            new Tag(5L, "testTag2")
        );
        Page<Tag> page = new Page<>(modelList, 1, 1);
        Mockito.when(newsRepository.existById(validId)).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> tagService.readByNewsId(validId, new PaginationDtoRequest(1, 5)));
    }
}
