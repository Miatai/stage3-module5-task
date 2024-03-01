package com.mjc.school.controller;

import com.mjc.school.controller.impl.NewsRestController;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.CommentService;
import com.mjc.school.service.NewsService;
import com.mjc.school.service.TagService;
import com.mjc.school.service.dto.*;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(NewsRestController.class)
public class NewsRestControllerTest {
    private final String BASE_URI = "/api/v1/news";
    @MockBean
    private NewsService newsService;
    @MockBean
    private TagService tagService;
    @MockBean
    private CommentService commentService;
    @MockBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void readAllNewsTest() {
        long id1 = 1L;
        long id2 = 2L;
        String title1 = "testNewsTitle1";
        String content1 = "testNewsContent1";
        String title2 = "testNewsTitle2";
        String content2 = "testNewsContent2";

        Mockito.when(newsService.readAll(any(), any(), any())).thenReturn(new PageDtoResponse<NewsDtoResponse>(List.of(
            new NewsDtoResponse(id1, title1, content1, LocalDateTime.now(), LocalDateTime.now(), null, null, null),
            new NewsDtoResponse(id2, title2, content2, LocalDateTime.now(), LocalDateTime.now(), null, null, null)
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
            .body("modelDtoList[0].title", Matchers.equalTo(title1))
            .body("modelDtoList[0].content", Matchers.equalTo(content1))
            .body("modelDtoList[1].id", Matchers.equalTo((int) id2))
            .body("modelDtoList[1].title", Matchers.equalTo(title2))
            .body("modelDtoList[1].content", Matchers.equalTo(content2));
        Mockito.verify(newsService).readAll(any(), any(), any());
    }

    @Test
    void readNewsByIdTest() {
        long id1 = 1L;
        String title1 = "testNewsTitle1";
        String content1 = "testNewsContent1";

        Mockito.when(newsService.readById(eq(id1))).thenReturn(
            new NewsDtoResponse(id1, title1, content1, LocalDateTime.now(), LocalDateTime.now(), null, null, null)
        );
        RestAssuredMockMvc.given()
            .when()
            .get(BASE_URI + "/1")
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo((int) id1))
            .body("title", Matchers.equalTo(title1))
            .body("content", Matchers.equalTo(content1));
        Mockito.verify(newsService).readById(eq(id1));
    }

    @Test
    void createNewsTest() {
        long id1 = 1L;
        String title1 = "testNewsTitle1";
        String content1 = "testNewsContent1";
        Mockito.when(newsService.create(any(NewsDtoCreateRequest.class))).thenReturn(
            new NewsDtoResponse(id1, title1, content1, LocalDateTime.now(), LocalDateTime.now(), null, null, null)
        );
        RestAssuredMockMvc.given()
            .contentType("application/json")
            .body(new NewsDtoCreateRequest(null, title1, content1, null, null, null))
            .when()
            .post(BASE_URI)
            .then()
            .statusCode(201)
            .body("id", Matchers.equalTo((int) id1))
            .body("title", Matchers.equalTo(title1))
            .body("content", Matchers.equalTo(content1));
        Mockito.verify(newsService).create(any(NewsDtoCreateRequest.class));
    }

    @Test
    void updateNewsTest() {
        long id1 = 1L;
        String title1 = "testNewsTitle1";
        String content1 = "testNewsContent1";
        Mockito.when(newsService.update(eq(id1), any(NewsDtoUpdateRequest.class))).thenReturn(
            new NewsDtoResponse(id1, title1, content1, LocalDateTime.now(), LocalDateTime.now(), null, null, null)
        );
        RestAssuredMockMvc.given()
            .contentType("application/json")
            .param("id", 1)
            .body(new NewsDtoUpdateRequest(title1, content1, null, null, null))
            .when()
            .patch(BASE_URI + "/" + (int) id1)
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo((int) id1))
            .body("title", Matchers.equalTo(title1))
            .body("content", Matchers.equalTo(content1));
        Mockito.verify(newsService).update(eq(id1), any(NewsDtoUpdateRequest.class));
    }

    @Test
    void deleteNewsTest() {
        long id1 = 1L;
        RestAssuredMockMvc.given()
            .param("id", 1)
            .when()
            .delete(BASE_URI + "/" + (int) id1)
            .then()
            .statusCode(204);
        Mockito.verify(newsService).deleteById(eq(id1));
    }

    @Test
    void readTagsByNewsIdTest(){
        long newsId = 1L;
        long id1 = 1L;
        long id2 = 2L;
        String name1 = "testTag1";
        String name2 = "testTag2";
        Mockito.when(tagService.readByNewsId(eq(newsId), any(), any())).thenReturn(new PageDtoResponse<TagDtoResponse>(List.of(
            new TagDtoResponse(id1, name1),
            new TagDtoResponse(id2, name2)
        ), 1, 1));

        RestAssuredMockMvc.given()
            .param("page", "1")
            .param("page-size", "5")
            .when()
            .get(BASE_URI + "/" + newsId + "/tags")
            .then()
            .statusCode(200)
            .body("modelDtoList.size()", Matchers.equalTo(2))
            .body("modelDtoList.size()", Matchers.equalTo(2))
            .body("modelDtoList[0].id", Matchers.equalTo((int) id1))
            .body("modelDtoList[0].name", Matchers.equalTo(name1))
            .body("modelDtoList[1].id", Matchers.equalTo((int) id2))
            .body("modelDtoList[1].name", Matchers.equalTo(name2));
        Mockito.verify(tagService).readByNewsId(eq(newsId), any(), any());
    }

    @Test
    void readCommentsByNewsIdTest(){
        long newsId = 1L;
        long id1 = 1L;
        long id2 = 2L;
        String content1 = "testComment1";
        String content2 = "testComment2";
        Mockito.when(commentService.readByNewsId(eq(newsId), any(), any())).thenReturn(new PageDtoResponse<CommentForNewsDtoResponse>(List.of(
            new CommentForNewsDtoResponse(id1, content1, LocalDateTime.now(),LocalDateTime.now()),
            new CommentForNewsDtoResponse(id2, content2,LocalDateTime.now(),LocalDateTime.now())
        ), 1, 1));

        RestAssuredMockMvc.given()
            .param("page", "1")
            .param("page-size", "5")
            .when()
            .get(BASE_URI + "/" + newsId + "/comments")
            .then()
            .statusCode(200)
            .body("modelDtoList.size()", Matchers.equalTo(2))
            .body("modelDtoList.size()", Matchers.equalTo(2))
            .body("modelDtoList[0].id", Matchers.equalTo((int) id1))
            .body("modelDtoList[0].content", Matchers.equalTo(content1))
            .body("modelDtoList[1].id", Matchers.equalTo((int) id2))
            .body("modelDtoList[1].content", Matchers.equalTo(content2));
        Mockito.verify(commentService).readByNewsId(eq(newsId), any(), any());
    }

    @Test
    void readAuthorByNewsIdTest(){
        long newsId = 1L;
        long id1 = 1L;
        String name1 = "testAuthorName1";

        Mockito.when(authorService.readByNewsId(eq(newsId))).thenReturn(
            new AuthorDtoResponse(id1,name1,LocalDateTime.now(),LocalDateTime.now())
        );

        RestAssuredMockMvc.given()
            .when()
            .get(BASE_URI + "/" + newsId + "/author")
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo((int) id1))
            .body("name", Matchers.equalTo(name1));
        Mockito.verify(authorService).readByNewsId(eq(newsId));
    }
}
