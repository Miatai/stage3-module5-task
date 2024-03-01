package com.mjc.school.controller;

import com.mjc.school.controller.impl.TagRestController;
import com.mjc.school.service.TagService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.PageDtoResponse;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(TagRestController.class)
public class TagRestControllerTest {
    private final String BASE_URI = "/api/v1/tags";
    @MockBean
    private TagService tagService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void readAllTagsTest() {
        long id1 = 1L;
        long id2 = 2L;
        String name1 = "testTag1";
        String name2 = "testTag2";

        Mockito.when(tagService.readAll(any(), any(), any())).thenReturn(new PageDtoResponse<TagDtoResponse>(List.of(
            new TagDtoResponse(id1, name1),
            new TagDtoResponse(id2, name2)
        ), 1, 1));
        RestAssuredMockMvc.given()
            .param("page", "1")
            .param("page-size", "5")
            .when()
            .get(BASE_URI)
            .then()
            .statusCode(200)
            .body("modelDtoList.size()", Matchers.equalTo(2))
            .body("modelDtoList[0].id", Matchers.equalTo((int) id1))
            .body("modelDtoList[0].name", Matchers.equalTo(name1))
            .body("modelDtoList[1].id", Matchers.equalTo((int) id2))
            .body("modelDtoList[1].name", Matchers.equalTo(name2));
        Mockito.verify(tagService).readAll(any(), any(), any());
    }

    @Test
    void readTagByIdTest() {
        long id1 = 1L;
        String name1 = "testTag1";

        Mockito.when(tagService.readById(id1)).thenReturn(
            new TagDtoResponse(id1, name1)
        );
        RestAssuredMockMvc.given()
            .when()
            .get(BASE_URI + "/1")
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo((int) id1))
            .body("name", Matchers.equalTo(name1));
        Mockito.verify(tagService).readById(id1);
    }

    @Test
    void createTagTest(){
        long id1 = 1L;
        String name1 = "testTag1";
        Mockito.when(tagService.create(any(TagDtoRequest.class))).thenReturn(
            new TagDtoResponse(id1, name1)
        );
        RestAssuredMockMvc.given()
            .contentType("application/json")
            .body(new AuthorDtoRequest(name1))
            .when()
            .post(BASE_URI)
            .then()
            .statusCode(201)
            .body("id", Matchers.equalTo((int) id1))
            .body("name", Matchers.equalTo(name1));
        Mockito.verify(tagService).create(any(TagDtoRequest.class));
    }

    @Test
    void updateTagTest() {
        long id1 = 1L;
        String name1 = "testTag1";
        Mockito.when(tagService.update(eq(id1), any(TagDtoRequest.class))).thenReturn(
            new TagDtoResponse(id1, name1)
        );
        RestAssuredMockMvc.given()
            .contentType("application/json")
            .param("id", 1)
            .body(new TagDtoRequest(name1))
            .when()
            .patch(BASE_URI + "/" + (int) id1)
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo((int) id1))
            .body("name", Matchers.equalTo(name1));
        Mockito.verify(tagService).update(eq(id1), any(TagDtoRequest.class));
    }

    @Test
    void deleteTagTest() {
        long id1 = 1L;
        RestAssuredMockMvc.given()
            .param("id", 1)
            .when()
            .delete(BASE_URI + "/" + (int) id1)
            .then()
            .statusCode(204);
        Mockito.verify(tagService).deleteById(eq(id1));
    }
}
