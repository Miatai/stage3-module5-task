package com.mjc.school.controller;

import com.mjc.school.controller.impl.CommentRestController;
import com.mjc.school.service.CommentService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.dto.PageDtoResponse;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
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

@WebMvcTest(CommentRestController.class)
public class CommentRestControllerTest {
    private final String BASE_PATH = "/api/v1/comments";
    private final String BASE_URI = "http://localhost:8081";

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void readAll_ResponseCode200_Test() {
        long id1 = 1L;
        long id2 = 2L;
        String content1 = "testComment1";
        String content2 = "testComment2";

        Mockito.when(commentService.readAll(any(), any(), any())).thenReturn(new PageDtoResponse<CommentDtoResponse>(List.of(
            new CommentDtoResponse(id1, content1, LocalDateTime.now(), LocalDateTime.now(), null),
            new CommentDtoResponse(id2, content2, LocalDateTime.now(), LocalDateTime.now(), null)
        ), 1, 1));
        RestAssuredMockMvc.given()
            .param("page", "1")
            .param("page-size", "5")
            .when()
            .get(BASE_PATH)
            .then()
            .statusCode(200)
            .body("modelDtoList.size()", Matchers.equalTo(2))
            .body("modelDtoList[0].id", Matchers.equalTo((int) id1))
            .body("modelDtoList[0].content", Matchers.equalTo(content1))
            .body("modelDtoList[1].id", Matchers.equalTo((int) id2))
            .body("modelDtoList[1].content", Matchers.equalTo(content2));
        Mockito.verify(commentService).readAll(any(), any(), any());
    }

    @Test
    void readAll_ResponseCode400_Test(){
        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given().param("sorting", "invalidField:desc");
        Response response = httpRequest.request(Method.GET, BASE_PATH);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    void readById_ResponseCode200_Test() {
        long id1 = 1L;
        String content1 = "testComment1";

        Mockito.when(commentService.readById(id1)).thenReturn(
            new CommentDtoResponse(id1, content1, LocalDateTime.now(), LocalDateTime.now(),null)
        );
        RestAssuredMockMvc.given()
            .when()
            .get(BASE_PATH + "/1")
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo((int) id1))
            .body("content", Matchers.equalTo(content1));
        Mockito.verify(commentService).readById(id1);
    }

    @Test
    void readById_ResponseCode404_Test(){
        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET, BASE_PATH + "/999");
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    void create_ResponseCode201_Test(){
        long id1 = 1L;
        String content1 = "testComment1";
        Mockito.when(commentService.create(any(CommentDtoRequest.class))).thenReturn(
            new CommentDtoResponse(id1, content1, LocalDateTime.now(), LocalDateTime.now(), null)
        );
        RestAssuredMockMvc.given()
            .contentType("application/json")
            .body(new CommentDtoRequest(content1, null))
            .when()
            .post(BASE_PATH)
            .then()
            .statusCode(201)
            .body("id", Matchers.equalTo((int) id1))
            .body("content", Matchers.equalTo(content1));
        Mockito.verify(commentService).create(any(CommentDtoRequest.class));
    }

    @Test
    void create_ResponseCode400_Test(){
        String invalidContent = "In";
        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given().header("Content-Type", "application/json");
        Response response = httpRequest.body(new CommentDtoRequest(invalidContent, 1L)).request(Method.POST, BASE_PATH);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    void update_ResponseCode200_Test() {
        long id1 = 1L;
        String content1 = "testComment1";
        Mockito.when(commentService.update(eq(id1), any(CommentDtoRequest.class))).thenReturn(
            new CommentDtoResponse(id1, content1, LocalDateTime.now(), LocalDateTime.now(), null)
        );
        RestAssuredMockMvc.given()
            .contentType("application/json")
            .param("id", 1)
            .body(new AuthorDtoRequest(content1))
            .when()
            .patch(BASE_PATH + "/" + (int) id1)
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo((int) id1))
            .body("content", Matchers.equalTo(content1));
        Mockito.verify(commentService).update(eq(id1), any(CommentDtoRequest.class));
    }

    @Test
    void update_ResponseCode400_Test(){
        String invalidContent = "In";
        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given().header("Content-Type", "application/json");
        Response response = httpRequest.body(new CommentDtoRequest(invalidContent, 1L)).request(Method.PATCH, BASE_PATH + "/1");
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    void update_ResponseCode404_Test(){
        String validContent = "Qwerty";
        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given().header("Content-Type", "application/json");
        Response response = httpRequest.body(new CommentDtoRequest(validContent, 1L)).request(Method.PATCH, BASE_PATH + "/999");
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    void delete_ResponseCode204_Test() {
        long id1 = 1L;
        RestAssuredMockMvc.given()
            .param("id", 1)
            .when()
            .delete(BASE_PATH + "/" + (int) id1)
            .then()
            .statusCode(204);
        Mockito.verify(commentService).deleteById(eq(id1));
    }

    @Test
    void deleteById_ResponseCode404_Test(){
        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.DELETE, BASE_PATH + "/999");
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }
}
