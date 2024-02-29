package com.mjc.school.service;

import com.mjc.school.service.dto.PageDtoResponse;

import java.util.List;

public interface BaseService<C, R, K, U, P, S, F> {
    PageDtoResponse<R> readAll(P paginationDtoRequest, S sortingDtoRequest, F searchFilterDtoRequest);

    R readById(K id);

    R create(C createRequest);

    R update(K id, U updateRequest);

    void deleteById(K id);
}
