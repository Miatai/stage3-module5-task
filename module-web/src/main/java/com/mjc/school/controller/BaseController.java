package com.mjc.school.controller;

import com.mjc.school.service.dto.PageDtoResponse;
import com.mjc.school.service.dto.PaginationDtoRequest;
import com.mjc.school.service.dto.SearchFilterDtoRequest;
import com.mjc.school.service.dto.SortingDtoRequest;

import java.util.List;

public interface BaseController<C, R, K, U> {

//    PageDtoResponse<R> readAll(PaginationDtoRequest paginationDtoRequest,
//                               SortingDtoRequest sortingDtoRequest,
//                               SearchFilterDtoRequest searchFilterDtoRequest);

    PageDtoResponse<R> readAll(int page,
                               int pageSize,
                               List<String> sortByAndOrder,
                               List<String> filters);

    R readById(K id);

    R create(C createRequest);

    R update(K id, U updateRequest);

    void deleteById(K id);
}
