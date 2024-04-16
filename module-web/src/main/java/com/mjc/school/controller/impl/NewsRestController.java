package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.CommentService;
import com.mjc.school.service.NewsService;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/news", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "News API", produces = MediaType.APPLICATION_JSON_VALUE)
public class NewsRestController implements BaseController<NewsDtoCreateRequest, NewsDtoResponse, Long, NewsDtoUpdateRequest> {
    private final NewsService newsService;
    private final TagService tagService;
    private final CommentService commentService;
    private final AuthorService authorService;


    @Autowired
    public NewsRestController(final NewsService newsService,
                              final TagService tagService,
                              final CommentService commentService,
                              final AuthorService authorService) {
        this.newsService = newsService;
        this.tagService = tagService;
        this.commentService = commentService;
        this.authorService = authorService;
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "View all news", response = PageDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved all news")
    })
    public PageDtoResponse<NewsDtoResponse> readAll(@RequestParam(value = "page", defaultValue = "1") int page,
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
        SearchFilterDtoRequest searchFilterDtoRequest = SearchFilterDtoRequest.builder()
            .filters(filters)
            .build();
        PageDtoResponse<NewsDtoResponse> pageDtoResponse = newsService.readAll(paginationDtoRequest, sortingDtoRequest, searchFilterDtoRequest);
        for (NewsDtoResponse dto : pageDtoResponse.getModelDtoList()) {
            addHateoasLinksToNewsDtoResponse(dto);
        }
        return pageDtoResponse;
    }

    @Override
    @GetMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve specific news with the supplied id", response = NewsDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved the news with the supplied id"),
        @ApiResponse(code = 404, message = "News with the supplied id not found")
    })
    public NewsDtoResponse readById(@PathVariable Long id) {
        return addHateoasLinksToNewsDtoResponse(newsService.readById(id));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a news", response = NewsDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created news")
    }
    )
    public NewsDtoResponse create(@RequestBody NewsDtoCreateRequest dtoRequest) {
        return addHateoasLinksToNewsDtoResponse(newsService.create(dtoRequest));
    }

    @Override
    @PatchMapping(value = "/{id:\\d+}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Patch news information", response = NewsDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated news with the supplied id"),
        @ApiResponse(code = 404, message = "News with the supplied id not found")
    }
    )
    public NewsDtoResponse update(@PathVariable Long id,
                                  @RequestBody NewsDtoUpdateRequest dtoRequest) {
        return addHateoasLinksToNewsDtoResponse(newsService.update(id, dtoRequest));
    }

    @Override
    @DeleteMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Deletes specific news with the supplied id")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully deleted news with the supplied id"),
        @ApiResponse(code = 404, message = "News with the supplied id not found")
    }
    )
    public void deleteById(@PathVariable Long id) {
        newsService.deleteById(id);
    }

    @GetMapping(value = "/{id:\\d+}/tags")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve specific tags with the supplied news id", response = PageDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved all tags with the supplied news id")
    }
    )
    public PageDtoResponse<TagDtoResponse> readTagsByNewsId(@PathVariable Long id,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "page-size", defaultValue = "10") int pageSize) {
        PaginationDtoRequest paginationDtoRequest = PaginationDtoRequest.builder()
            .page(page)
            .pageSize(pageSize)
            .build();
        PageDtoResponse<TagDtoResponse> pageDtoResponse = tagService.readByNewsId(id, paginationDtoRequest);
        pageDtoResponse.setModelDtoList(pageDtoResponse.getModelDtoList().stream().map(TagRestController::addHateoasLinksToTagDtoResponse)
            .collect(Collectors.toList()));
        return pageDtoResponse;
    }

    @GetMapping(value = "/{id:\\d+}/comments")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve specific comments with the supplied news id", response = PageDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved all comments with the supplied news id")
    }
    )
    public PageDtoResponse<CommentForNewsDtoResponse> readCommentsByNewsId(@PathVariable Long id,
                                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                                           @RequestParam(value = "page-size", defaultValue = "10") int pageSize,
                                                                           @RequestParam(value = "sorting", required = false) List<String> sortByAndOrder) {
        PaginationDtoRequest paginationDtoRequest = PaginationDtoRequest.builder()
            .page(page)
            .pageSize(pageSize)
            .build();
        SortingDtoRequest sortingDtoRequest = SortingDtoRequest.builder()
            .sortByAndOrder(sortByAndOrder)
            .build();
        PageDtoResponse<CommentForNewsDtoResponse> pageDtoResponse = commentService.readByNewsId(id, paginationDtoRequest, sortingDtoRequest);
        pageDtoResponse.setModelDtoList(pageDtoResponse.getModelDtoList().stream().map(CommentRestController::addHateoasLinksToCommentForNewsDtoResponse)
            .collect(Collectors.toList()));
        return pageDtoResponse;
    }

    @GetMapping(value = "/{id:\\d+}/author")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve specific author with the supplied news id", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved specific author with the supplied news id"),
        @ApiResponse(code = 404, message = "Author with the supplied news id not found")
    }
    )
    public AuthorDtoResponse readAuthorByNewsId(@PathVariable Long id) {

        return AuthorRestController.addHateoasLinksToAuthorDtoResponse(authorService.readByNewsId(id));
    }

    static NewsDtoResponse addHateoasLinksToNewsDtoResponse(NewsDtoResponse dtoResponse) {
        dtoResponse.add(linkTo(methodOn(NewsRestController.class).readById(dtoResponse.getId())).withSelfRel());
        dtoResponse.add(linkTo(methodOn(NewsRestController.class).readTagsByNewsId(dtoResponse.getId(), 1, 10)).withRel("tagsNames"));
        dtoResponse.add(linkTo(methodOn(NewsRestController.class).readCommentsByNewsId(dtoResponse.getId(),
            1, 10, Collections.emptyList())).withRel("comments"));
        dtoResponse.add(linkTo(methodOn(NewsRestController.class).readAuthorByNewsId(dtoResponse.getId())).withRel("authorName"));
        return dtoResponse;
    }
}
