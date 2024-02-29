package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.CommentService;
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
@RequestMapping(value = "/api/v1/comments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Comment Rest Controller", description = "Operations for creating, updating, retrieving and deleting comments in the application")
public class CommentRestController implements BaseController<CommentDtoRequest, CommentDtoResponse, Long, CommentDtoRequest> {
    private final CommentService commentService;

    @Autowired
    public CommentRestController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "View all comments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all comments"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public PageDtoResponse<CommentDtoResponse> readAll(@Parameter(description = "Page number", example = "1", required = false)
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
        PageDtoResponse<CommentDtoResponse> pageDtoResponse = commentService.readAll(paginationDtoRequest, sortingDtoRequest, searchFilterDtoRequest);
        pageDtoResponse.setModelDtoList(pageDtoResponse.getModelDtoList().stream().map(CommentRestController::addHateoasLinksToCommentDtoResponse)
            .collect(Collectors.toList()));
        return pageDtoResponse;
    }

    @Override
    @GetMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve specific comment with the supplied id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the comment with the supplied id"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public CommentDtoResponse readById(@PathVariable
                                       @Parameter(name = "id", description = "Comment id", example = "1")
                                       Long id) {
        return addHateoasLinksToCommentDtoResponse(commentService.readById(id));
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a comment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created a comment"),
        @ApiResponse(responseCode = "400", description = "The request parameters are invalid"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public CommentDtoResponse create(@RequestBody CommentDtoRequest createRequest) {
        return addHateoasLinksToCommentDtoResponse(commentService.create(createRequest));
    }

    @Override
    @PutMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update comment information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated comment information"),
        @ApiResponse(responseCode = "400", description = "The request parameters are invalid"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public CommentDtoResponse update(@PathVariable
                                     @Parameter(name = "id", description = "Comment id", example = "1")
                                     Long id,
                                     @RequestBody CommentDtoRequest updateRequest) {
        return addHateoasLinksToCommentDtoResponse(commentService.update(id, updateRequest));
    }

    @Override
    @DeleteMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes specific comment with the supplied id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the specific comment"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public void deleteById(@PathVariable
                           @Parameter(name = "id", description = "Comment id", example = "1")
                           Long id) {
        commentService.deleteById(id);
    }

    static CommentDtoResponse addHateoasLinksToCommentDtoResponse(CommentDtoResponse dtoResponse) {
        dtoResponse.add(linkTo(methodOn(CommentRestController.class).readById(dtoResponse.getId())).withSelfRel());
        dtoResponse.add(linkTo((methodOn(CommentRestController.class).readAll(1, 10, Collections.emptyList(), Collections.emptyList()))).withRel("collection"));
        return dtoResponse;
    }

    static CommentForNewsDtoResponse addHateoasLinksToCommentForNewsDtoResponse(CommentForNewsDtoResponse forNewsDtoResponse) {
        forNewsDtoResponse.add(linkTo(methodOn(CommentRestController.class).readById(forNewsDtoResponse.getId())).withSelfRel());
        return forNewsDtoResponse;
    }

}
