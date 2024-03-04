package com.mjc.school.controller;

import com.mjc.school.controller.impl.AuthorRestController;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.dto.*;
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

@WebMvcTest(AuthorRestController.class)
public class AuthorRestControllerTest {
    private final String BASE_PATH = "/api/v1/authors";
    private final String BASE_URI = "http://localhost:8081";
    @MockBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void readAll_ResponseCode200_Test() {
        long id1 = 1L;
        String name1 = "testAuthorName1";
        long id2 = 1L;
        String name2 = "testAuthorName1";
        Mockito.when(authorService.readAll(any(), any(), any()
        )).thenReturn(new PageDtoResponse<AuthorDtoResponse>(List.of(
            new AuthorDtoResponse(id1, name1, LocalDateTime.now(), LocalDateTime.now()),
            new AuthorDtoResponse(id2, name2, LocalDateTime.now(), LocalDateTime.now()))
            , 1, 1));
        RestAssuredMockMvc.given()
            .param("page", "1")
            .param("page-size", "5")
            .when()
            .get(BASE_PATH)
            .then()
            .statusCode(200)
            .body("modelDtoList.size()", Matchers.equalTo(2))
            .body("modelDtoList[0].id", Matchers.equalTo((int) id1))
            .body("modelDtoList[0].name", Matchers.equalTo(name1))
            .body("modelDtoList[1].id", Matchers.equalTo((int) id2))
            .body("modelDtoList[1].name", Matchers.equalTo(name2));
        Mockito.verify(authorService).readAll(any(), any(), any());
    }



    @Test
    void readById_ResponseCode200_Test() {
        long id1 = 1L;
        String name1 = "testAuthorName1";
        Mockito.when(authorService.readById(eq(id1))).thenReturn(
            new AuthorDtoResponse(id1, name1, LocalDateTime.now(), LocalDateTime.now())
        );
        RestAssuredMockMvc.given()
            .when()
            .get(BASE_PATH + "/1")
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo((int) id1))
            .body("name", Matchers.equalTo(name1));
        Mockito.verify(authorService).readById(eq(id1));
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
    void create_ResponseCode201_Test() {
        long id1 = 1L;
        String name1 = "testAuthorName1";
        Mockito.when(authorService.create(any(AuthorDtoRequest.class))).thenReturn(
            new AuthorDtoResponse(id1, name1, LocalDateTime.now(), LocalDateTime.now())
        );
        RestAssuredMockMvc.given()
            .contentType("application/json")
            .body(new AuthorDtoRequest(name1))
            .when()
            .post(BASE_PATH)
            .then()
            .statusCode(201)
            .body("id", Matchers.equalTo((int) id1))
            .body("name", Matchers.equalTo(name1));
        Mockito.verify(authorService).create(any(AuthorDtoRequest.class));
    }

    @Test
    void create_ResponseCode400_Test(){
        String invalidName = "In";
        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given().header("Content-Type", "application/json");
        Response response = httpRequest.body(new AuthorDtoRequest(invalidName)).request(Method.POST, BASE_PATH);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    void update_ResponseCode200_Test() {
        long id1 = 1L;
        String name1 = "testAuthorName1";
        Mockito.when(authorService.update(eq(id1), any(AuthorDtoRequest.class))).thenReturn(
            new AuthorDtoResponse(id1, name1, LocalDateTime.now(), LocalDateTime.now())
        );
        RestAssuredMockMvc.given()
            .contentType("application/json")
            .param("id", (int) id1)
            .body(new AuthorDtoRequest(name1))
            .when()
            .patch(BASE_PATH + "/" + (int) id1)
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo((int) id1))
            .body("name", Matchers.equalTo(name1));
        Mockito.verify(authorService).update(eq(id1), any(AuthorDtoRequest.class));
    }

    @Test
    void update_ResponseCode400_Test(){
        String invalidName = "In";
        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given().header("Content-Type", "application/json");
        Response response = httpRequest.body(new AuthorDtoRequest(invalidName)).request(Method.PATCH, BASE_PATH + "/1");
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    void update_ResponseCode404_Test(){
        String validName = "Qwerty";
        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given().header("Content-Type", "application/json");
        Response response = httpRequest.body(new AuthorDtoRequest(validName)).request(Method.PATCH, BASE_PATH + "/999");
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
        Mockito.verify(authorService).deleteById(eq(id1));
    }

    @Test
    void deleteById_ResponseCode404_Test(){
        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.DELETE, BASE_PATH + "/999");
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    void readWithNewsCount_ResponseCode200_Test() {
        long id1 = 1L;
        String name1 = "testAuthorName1";
        long newsCount1 = 5L;
        long id2 = 1L;
        String name2 = "testAuthorName1";
        long newsCount2 = 8L;
        Mockito.when(authorService.readWithNewsCount(any(PaginationDtoRequest.class)))
            .thenReturn(new PageDtoResponse<AuthorWithNewsCountDtoResponse>(List.of(
            new AuthorWithNewsCountDtoResponse(id1, name1, LocalDateTime.now(), LocalDateTime.now(), newsCount1),
            new AuthorWithNewsCountDtoResponse(id2, name2, LocalDateTime.now(), LocalDateTime.now(), newsCount2))
            , 1, 1));
        RestAssuredMockMvc.given()
            .param("page", "1")
            .param("page-size", "5")
            .when()
            .get(BASE_PATH + "/news-count")
            .then()
            .statusCode(200)
            .body("modelDtoList.size()", Matchers.equalTo(2))
            .body("modelDtoList[0].id", Matchers.equalTo((int) id1))
            .body("modelDtoList[0].name", Matchers.equalTo(name1))
            .body("modelDtoList[0].newsCount", Matchers.equalTo((int) newsCount1))
            .body("modelDtoList[1].id", Matchers.equalTo((int) id1))
            .body("modelDtoList[1].name", Matchers.equalTo(name2))
            .body("modelDtoList[1].newsCount", Matchers.equalTo((int) newsCount2));
        Mockito.verify(authorService).readWithNewsCount(any(PaginationDtoRequest.class));
    }
}
