package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(tags = "Author API", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorRestController implements BaseController<AuthorDtoRequest, AuthorDtoResponse, Long, AuthorDtoRequest> {
    private final AuthorService authorService;

    @Autowired
    public AuthorRestController(final AuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "View all authors", response = PageDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved all authors")
    })
    public PageDtoResponse<AuthorDtoResponse> readAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                      @RequestParam(value = "page-size", defaultValue = "10") int pageSize,
                                                      @RequestParam(value = "sorting", required = false) List<String> sortByAndOrder,
                                                      @RequestParam(value = "filter", required = false) List<String> filters
    ) {
        PaginationDtoRequest paginationDtoRequest = PaginationDtoRequest.builder()
            .page(page)
            .pageSize(pageSize)
            .build();
        SearchFilterDtoRequest searchFilterDtoRequest = SearchFilterDtoRequest.builder()
            .filters(filters)
            .build();
        PageDtoResponse<AuthorDtoResponse> pageDtoResponse = authorService.readAll(paginationDtoRequest, null, searchFilterDtoRequest);
        pageDtoResponse.setModelDtoList(pageDtoResponse.getModelDtoList().stream().map(AuthorRestController::addHateoasLinksToAuthorDtoResponse)
            .collect(Collectors.toList()));
        return pageDtoResponse;
    }

    @Override
    @GetMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "View author by supplied id", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved the author with the supplied id"),
        @ApiResponse(code = 404, message = "Author with the supplied id not found")
    })
    public AuthorDtoResponse readById(@PathVariable Long id) {
        return addHateoasLinksToAuthorDtoResponse(authorService.readById(id));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create author", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created author")
    })
    public AuthorDtoResponse create(@RequestBody AuthorDtoRequest dtoRequest) {
        return addHateoasLinksToAuthorDtoResponse(authorService.create(dtoRequest));
    }

    @Override
    @PatchMapping(value = "/{id:\\d+}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update author by supplied id", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated author with the supplied id"),
        @ApiResponse(code = 404, message = "Author with the supplied id not found")
    })
    public AuthorDtoResponse update(@PathVariable Long id,
                                    @RequestBody AuthorDtoRequest dtoRequest) {
        return addHateoasLinksToAuthorDtoResponse(authorService.update(id, dtoRequest));
    }

    @Override
    @DeleteMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete author by supplied id", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully deleted author with the supplied id"),
        @ApiResponse(code = 404, message = "Author with the supplied id not found")
    })
    public void deleteById(@PathVariable Long id) {
        authorService.deleteById(id);
    }

    @GetMapping(value = "/news-count")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "View all authors with amount of written news", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved all authors with amount of written news"),
    }
    )
    public PageDtoResponse<AuthorWithNewsCountDtoResponse> readWithNewsCount(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                             @RequestParam(value = "page-size", defaultValue = "10") int pageSize) {
        PaginationDtoRequest paginationDtoRequest = PaginationDtoRequest.builder()
            .page(page)
            .pageSize(pageSize)
            .build();
        PageDtoResponse<AuthorWithNewsCountDtoResponse> pageDtoResponse = authorService.readWithNewsCount(paginationDtoRequest);
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
