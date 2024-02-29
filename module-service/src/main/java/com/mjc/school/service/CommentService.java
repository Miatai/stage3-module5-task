package com.mjc.school.service;

import com.mjc.school.service.dto.*;

import java.util.List;

public interface CommentService extends BaseService<CommentDtoRequest, CommentDtoResponse, Long, CommentDtoRequest, PaginationDtoRequest, SortingDtoRequest, SearchFilterDtoRequest> {
    PageDtoResponse<CommentForNewsDtoResponse> readByNewsId(Long newsId, PaginationDtoRequest paginationDtoRequest, SortingDtoRequest sortingDtoRequest);
}
