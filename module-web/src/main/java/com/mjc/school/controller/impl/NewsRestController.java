package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.CommentService;
import com.mjc.school.service.NewsService;
import com.mjc.school.service.TagService;
import com.mjc.school.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
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
@Tag(name = "News Rest Controller", description = "Operations for creating, updating, retrieving and deleting news and retrieving component of news in the application")
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
    @Operation(summary = "View all news")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all news"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public PageDtoResponse<NewsDtoResponse> readAll(@Parameter(description = "Page number", example = "1", required = false)
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
        PageDtoResponse<NewsDtoResponse> pageDtoResponse = newsService.readAll(paginationDtoRequest, sortingDtoRequest, searchFilterDtoRequest);
        for (NewsDtoResponse dto : pageDtoResponse.getModelDtoList()) {
            addHateoasLinksToNewsDtoResponse(dto);
        }
        return pageDtoResponse;
    }

    @Override
    @GetMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve specific news with the supplied id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the news with the supplied id"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public NewsDtoResponse readById(@PathVariable
                                    @Parameter(name = "id", description = "News id", example = "1")
                                    Long id) {
        return addHateoasLinksToNewsDtoResponse(newsService.readById(id));
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a news")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created a news"),
        @ApiResponse(responseCode = "400", description = "The request parameters are invalid"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public NewsDtoResponse create(@RequestBody NewsDtoCreateRequest dtoRequest) {
        return addHateoasLinksToNewsDtoResponse(newsService.create(dtoRequest));
    }

    @Override
    @PutMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update news information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated news information"),
        @ApiResponse(responseCode = "400", description = "The request parameters are invalid"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public NewsDtoResponse update(@PathVariable
                                  @Parameter(name = "id", description = "News id", example = "1")
                                  Long id,
                                  @RequestBody NewsDtoUpdateRequest dtoRequest) {
        return addHateoasLinksToNewsDtoResponse(newsService.update(id, dtoRequest));
    }

    @Override
    @DeleteMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes specific news with the supplied id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the specific news"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public void deleteById(@PathVariable
                           @Parameter(name = "id", description = "Product id", example = "1")
                           Long id) {
        newsService.deleteById(id);
    }

    @GetMapping(value = "/{id:\\d+}/tags")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve specific tags with the supplied news id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved specific tags with the supplied news id"),
        @ApiResponse(responseCode = "400", description = "The request parameters are invalid"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public PageDtoResponse<TagDtoResponse> readTagsByNewsId(@PathVariable
                                                            @Parameter(name = "id", description = "News id", example = "1")
                                                            Long id,
                                                            @Parameter(description = "Page number", example = "1", required = false)
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
        PageDtoResponse<TagDtoResponse> pageDtoResponse = tagService.readByNewsId(id, paginationDtoRequest, sortingDtoRequest);
        pageDtoResponse.setModelDtoList(pageDtoResponse.getModelDtoList().stream().map(TagRestController::addHateoasLinksToTagDtoResponse)
            .collect(Collectors.toList()));
        return pageDtoResponse;
    }

    @GetMapping(value = "/{id:\\d+}/comments")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve specific comments with the supplied news id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved specific comments with the supplied news id"),
        @ApiResponse(responseCode = "400", description = "The request parameters are invalid"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public PageDtoResponse<CommentForNewsDtoResponse> readCommentsByNewsId(@PathVariable
                                                                           @Parameter(name = "id", description = "News id", example = "1")
                                                                           Long id,
                                                                           @Parameter(description = "Page number", example = "1", required = false)
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
        PageDtoResponse<CommentForNewsDtoResponse> pageDtoResponse = commentService.readByNewsId(id, paginationDtoRequest, sortingDtoRequest);
        pageDtoResponse.setModelDtoList(pageDtoResponse.getModelDtoList().stream().map(CommentRestController::addHateoasLinksToCommentForNewsDtoResponse)
            .collect(Collectors.toList()));
        return pageDtoResponse;
    }

    @GetMapping(value = "/{id:\\d+}/author")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve specific author with the supplied news id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved specific author with the supplied news id"),
        @ApiResponse(responseCode = "400", description = "The request parameters are invalid"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public AuthorDtoResponse readAuthorByNewsId(@PathVariable
                                                @Parameter(name = "id", description = "News id", example = "1")
                                                Long id) {

        return AuthorRestController.addHateoasLinksToAuthorDtoResponse(authorService.readByNewsId(id));
    }

    static NewsDtoResponse addHateoasLinksToNewsDtoResponse(NewsDtoResponse dtoResponse) {
        dtoResponse.add(linkTo(methodOn(NewsRestController.class).readById(dtoResponse.getId())).withSelfRel());
        dtoResponse.add(linkTo(methodOn(NewsRestController.class).readTagsByNewsId(dtoResponse.getId(), 1, 10, Collections.emptyList())).withRel("tagsNames"));
        dtoResponse.add(linkTo(methodOn(NewsRestController.class).readCommentsByNewsId(dtoResponse.getId(),
            1, 10, Collections.emptyList())).withRel("comments"));
        dtoResponse.add(linkTo(methodOn(NewsRestController.class).readAuthorByNewsId(dtoResponse.getId())).withRel("authorName"));
        return dtoResponse;
    }
}
