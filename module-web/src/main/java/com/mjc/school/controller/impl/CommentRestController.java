package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.CommentService;
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
@RequestMapping(value = "/api/v1/comments", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(produces = "application/json", value = "Operations for creating, patching, retrieving and deleting comments in the application")
public class CommentRestController implements BaseController<CommentDtoRequest, CommentDtoResponse, Long, CommentDtoRequest> {
    private final CommentService commentService;

    @Autowired
    public CommentRestController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "View all comments", response = PageDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved all comments"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public PageDtoResponse<CommentDtoResponse> readAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "page-size", defaultValue = "10") int pageSize,
                                                       @RequestParam(value = "sorting", required = false) List<String> sortByAndOrder,
                                                       @RequestParam(value = "filter", required = false) List<String> filters
    ) {
        PaginationDtoRequest paginationDtoRequest = PaginationDtoRequest.builder()
            .page(page)
            .pageSize(pageSize)
            .build();
        SortingDtoRequest sortingDtoRequest = SortingDtoRequest.builder()
            .sortByAndOrder(sortByAndOrder)
            .build();
        PageDtoResponse<CommentDtoResponse> pageDtoResponse = commentService.readAll(paginationDtoRequest, sortingDtoRequest, null);
        pageDtoResponse.setModelDtoList(pageDtoResponse.getModelDtoList().stream().map(CommentRestController::addHateoasLinksToCommentDtoResponse)
            .collect(Collectors.toList()));
        return pageDtoResponse;
    }

    @Override
    @GetMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve specific comment with the supplied id", response = CommentDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved the comment with the supplied id"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
        @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public CommentDtoResponse readById(@PathVariable Long id) {
        return addHateoasLinksToCommentDtoResponse(commentService.readById(id));
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a comment", response = CommentDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created a comment"),
        @ApiResponse(code = 400, message = "The request parameters are invalid"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public CommentDtoResponse create(@RequestBody CommentDtoRequest createRequest) {
        return addHateoasLinksToCommentDtoResponse(commentService.create(createRequest));
    }

    @Override
    @PatchMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Patch comment information", response = CommentDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully patched comment information"),
        @ApiResponse(code = 400, message = "The request parameters are invalid"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
        @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public CommentDtoResponse update(@PathVariable Long id,
                                     @RequestBody CommentDtoRequest updateRequest) {
        return addHateoasLinksToCommentDtoResponse(commentService.update(id, updateRequest));
    }

    @Override
    @DeleteMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Deletes specific comment with the supplied id", response = CommentDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully deleted the specific comment"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
        @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public void deleteById(@PathVariable Long id) {
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
