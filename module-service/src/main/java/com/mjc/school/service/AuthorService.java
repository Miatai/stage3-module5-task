package com.mjc.school.service;

import com.mjc.school.service.dto.*;

import java.util.List;

public interface AuthorService extends BaseService<AuthorDtoRequest, AuthorDtoResponse, Long, AuthorDtoRequest, PaginationDtoRequest, SortingDtoRequest, SearchFilterDtoRequest> {
    AuthorDtoResponse readByNewsId(Long newsId);

    PageDtoResponse<AuthorWithNewsCountDtoResponse> readWithNewsCount(PaginationDtoRequest paginationDtoRequest, SortingDtoRequest sortingDtoRequest);
}
