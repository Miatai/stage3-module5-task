package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.TagService;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tag Rest Controller", description = "Operations for creating, updating, retrieving and deleting tags in the application")
public class TagRestController implements BaseController<TagDtoRequest, TagDtoResponse, Long, TagDtoRequest> {
    private final TagService tagService;

    @Autowired
    public TagRestController(final TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "View all tags")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all tags"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public PageDtoResponse<TagDtoResponse> readAll(@Parameter(description = "Page number", example = "1", required = false)
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
        PageDtoResponse<TagDtoResponse> pageDtoResponse = tagService.readAll(paginationDtoRequest, sortingDtoRequest, searchFilterDtoRequest);
        pageDtoResponse.setModelDtoList(pageDtoResponse.getModelDtoList().stream().map(TagRestController::addHateoasLinksToTagDtoResponse)
            .collect(Collectors.toList()));
        return pageDtoResponse;
    }

    @Override
    @GetMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve specific tag with the supplied id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the tag with the supplied id"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public TagDtoResponse readById(@PathVariable
                                   @Parameter(name = "id", description = "Tag id", example = "1")
                                   Long id) {
        return addHateoasLinksToTagDtoResponse(tagService.readById(id));
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a tag")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created a tag"),
        @ApiResponse(responseCode = "400", description = "The request parameters are invalid"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public TagDtoResponse create(@RequestBody TagDtoRequest dtoRequest) {
        return addHateoasLinksToTagDtoResponse(tagService.create(dtoRequest));
    }

    @Override
    @PutMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update tag information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated tag information"),
        @ApiResponse(responseCode = "400", description = "The request parameters are invalid"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public TagDtoResponse update(@PathVariable
                                 @Parameter(name = "id", description = "Tag id", example = "1")
                                 Long id,
                                 @RequestBody TagDtoRequest dtoRequest) {
        return addHateoasLinksToTagDtoResponse(tagService.update(id, dtoRequest));
    }

    @Override
    @DeleteMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes specific tag with the supplied id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the specific tag"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public void deleteById(@PathVariable
                           @Parameter(name = "id", description = "Tag id", example = "1")
                           Long id) {
        tagService.deleteById(id);
    }

    static TagDtoResponse addHateoasLinksToTagDtoResponse(TagDtoResponse dtoResponse) {
        dtoResponse.add(linkTo(methodOn(TagRestController.class).readById(dtoResponse.getId())).withSelfRel());
        return dtoResponse;
    }
}
