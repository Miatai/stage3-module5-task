package com.mjc.school.service;

import java.util.List;

import com.mjc.school.service.dto.*;

public interface TagService extends BaseService<TagDtoRequest, TagDtoResponse, Long, TagDtoRequest, PaginationDtoRequest, SortingDtoRequest, SearchFilterDtoRequest> {
    PageDtoResponse<TagDtoResponse> readByNewsId(Long newsId, PaginationDtoRequest paginationDtoRequest, SortingDtoRequest sortingDtoRequest);
}
