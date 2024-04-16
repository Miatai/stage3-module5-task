package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.TagService;
import com.mjc.school.service.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(tags = "Tag API", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagRestController implements BaseController<TagDtoRequest, TagDtoResponse, Long, TagDtoRequest> {
    private final TagService tagService;

    @Autowired
    public TagRestController(final TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "View all tags", response = PageDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved all tags")
    })
    public PageDtoResponse<TagDtoResponse> readAll(@RequestParam(value = "page", defaultValue = "1") int page,
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
        PageDtoResponse<TagDtoResponse> pageDtoResponse = tagService.readAll(paginationDtoRequest, null, searchFilterDtoRequest);
        pageDtoResponse.setModelDtoList(pageDtoResponse.getModelDtoList().stream().map(TagRestController::addHateoasLinksToTagDtoResponse)
            .collect(Collectors.toList()));
        return pageDtoResponse;
    }

    @Override
    @GetMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve specific tag with the supplied id", response = TagDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved the tag with the supplied id"),
        @ApiResponse(code = 404, message = "Tag with the supplied id not found")
    })
    public TagDtoResponse readById(@PathVariable Long id) {
        return addHateoasLinksToTagDtoResponse(tagService.readById(id));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a tag", response = TagDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created tag")
    })
    public TagDtoResponse create(@RequestBody TagDtoRequest dtoRequest) {
        return addHateoasLinksToTagDtoResponse(tagService.create(dtoRequest));
    }

    @Override
    @PatchMapping(value = "/{id:\\d+}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Patch tag information", response = TagDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated tag with the supplied id"),
        @ApiResponse(code = 404, message = "Tag with the supplied id not found")
    })
    public TagDtoResponse update(@PathVariable Long id,
                                 @RequestBody TagDtoRequest dtoRequest) {
        return addHateoasLinksToTagDtoResponse(tagService.update(id, dtoRequest));
    }

    @Override
    @DeleteMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Deletes specific tag with the supplied id")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully deleted tag with the supplied id"),
        @ApiResponse(code = 404, message = "Tag with the supplied id not found")
    })
    public void deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
    }

    static TagDtoResponse addHateoasLinksToTagDtoResponse(TagDtoResponse dtoResponse) {
        dtoResponse.add(linkTo(methodOn(TagRestController.class).readById(dtoResponse.getId())).withSelfRel());
        return dtoResponse;
    }
}
