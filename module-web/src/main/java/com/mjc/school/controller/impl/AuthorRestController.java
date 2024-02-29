package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/authors", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Author Rest Controller", description = "Operations for creating, updating, retrieving, deleting and retrieving with amount of written news authors in the application")
public class AuthorRestController implements BaseController<AuthorDtoRequest, AuthorDtoResponse, Long, AuthorDtoRequest> {
    private final AuthorService authorService;

    @Autowired
    public AuthorRestController(final AuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "View all authors")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all authors"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public PageDtoResponse<AuthorDtoResponse> readAll(@Parameter(description = "Page number", example = "1", required = false)
                                                      @RequestParam(value = "page", defaultValue = "0")
                                                      int page,
                                                      @Parameter(description = "Page size", example = "10", required = false)
                                                      @RequestParam(value = "page-size", defaultValue = "10")
                                                      int pageSize,
                                                      @Parameter(description = "Sorting field and sort type", example = "fieldName:sortType", required = false)
                                                      @RequestParam(value = "sorting", required = false)
                                                      List<String> sortByAndOrder,
                                                      @Parameter(description = "Field and value for filtering", example = "fieldName:value", required = false)
                                                      @RequestParam(value = "filter", required = false)
                                                      List<String> filters
    ) {
        PaginationDtoRequest paginationDtoRequest = PaginationDtoRequest.builder()
            .page(page)
            .pageSize(pageSize)
            .build();
        SortingDtoRequest sortingDtoRequest = SortingDtoRequest.builder()
            .sortByAndOrder(sortByAndOrder)
            .build();
        SearchFilterDtoRequest searchFilterDtoRequest = SearchFilterDtoRequest.builder()
            .filters(filters)
            .build();
        PageDtoResponse<AuthorDtoResponse> pageDtoResponse = authorService.readAll(paginationDtoRequest, sortingDtoRequest, searchFilterDtoRequest);
        pageDtoResponse.setModelDtoList(pageDtoResponse.getModelDtoList().stream().map(AuthorRestController::addHateoasLinksToAuthorDtoResponse)
            .collect(Collectors.toList()));
        return pageDtoResponse;
    }

    @Override
    @GetMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve specific author with the supplied id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the author with the supplied id"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public AuthorDtoResponse readById(@PathVariable
                                      @Parameter(name = "id", description = "Author id", example = "1")
                                      Long id) {
        return addHateoasLinksToAuthorDtoResponse(authorService.readById(id));
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an author")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created a author"),
        @ApiResponse(responseCode = "400", description = "The request parameters are invalid"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public AuthorDtoResponse create(@RequestBody AuthorDtoRequest dtoRequest) {
        return addHateoasLinksToAuthorDtoResponse(authorService.create(dtoRequest));
    }

    @Override
    @PutMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update author information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated author information"),
        @ApiResponse(responseCode = "400", description = "The request parameters are invalid"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public AuthorDtoResponse update(@PathVariable
                                    @Parameter(name = "id", description = "Author id", example = "1")
                                    Long id,
                                    @RequestBody AuthorDtoRequest dtoRequest) {
        return addHateoasLinksToAuthorDtoResponse(authorService.update(id, dtoRequest));
    }

    @Override
    @DeleteMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes specific author with the supplied id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the specific author"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public void deleteById(@PathVariable
                           @Parameter(name = "id", description = "Author id", example = "1")
                           Long id) {
        authorService.deleteById(id);
    }

    @GetMapping(value = "/news-count")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "View all authors with amount of written news")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all authors with amount of written news"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public PageDtoResponse<AuthorWithNewsCountDtoResponse> readWithNewsCount(@Parameter(description = "Page number", example = "1", required = false)
                                                                             @RequestParam(value = "page", defaultValue = "0")
                                                                             int page,
                                                                             @Parameter(description = "Page size", example = "10", required = false)
                                                                             @RequestParam(value = "page-size", defaultValue = "10")
                                                                             int pageSize,
                                                                             @Parameter(description = "Sorting field and sort type", example = "fieldName:sortType", required = false)
                                                                             @RequestParam(value = "sorting", required = false)
                                                                             List<String> sortByAndOrder) {


        PaginationDtoRequest paginationDtoRequest = PaginationDtoRequest.builder()
            .page(page)
            .pageSize(pageSize)
            .build();
        SortingDtoRequest sortingDtoRequest = SortingDtoRequest.builder()
            .sortByAndOrder(sortByAndOrder)
            .build();
        PageDtoResponse<AuthorWithNewsCountDtoResponse> pageDtoResponse = authorService.readWithNewsCount(paginationDtoRequest, sortingDtoRequest);
        pageDtoResponse.setModelDtoList(pageDtoResponse.getModelDtoList().stream().map(AuthorRestController::addHateoasLinksToAuthorDtoResponse)
            .collect(Collectors.toList()));
        return pageDtoResponse;
    }

    static AuthorDtoResponse addHateoasLinksToAuthorDtoResponse(AuthorDtoResponse dtoResponse) {
        dtoResponse.add(linkTo(methodOn(AuthorRestController.class).readById(dtoResponse.getId())).withSelfRel());
        dtoResponse.add(linkTo(methodOn(AuthorRestController.class).readAll(1, 10, Collections.emptyList(), Collections.emptyList())).withRel("collection"));
        return dtoResponse;
    }

    static AuthorWithNewsCountDtoResponse addHateoasLinksToAuthorDtoResponse(AuthorWithNewsCountDtoResponse withNewsCountDtoResponse) {
        withNewsCountDtoResponse.add(linkTo(methodOn(AuthorRestController.class).readById(withNewsCountDtoResponse.getId())).withSelfRel());
        return withNewsCountDtoResponse;
    }
}
