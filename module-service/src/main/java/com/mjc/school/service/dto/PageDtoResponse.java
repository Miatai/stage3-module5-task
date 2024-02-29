package com.mjc.school.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class PageDtoResponse<T> {
    private List<T> modelDtoList;
    private int currentPage;
    private int pageCount;

    public PageDtoResponse() {
        if (modelDtoList == null) {
            modelDtoList = new ArrayList<>();
        }
    }

    public PageDtoResponse(List<T> modelDtoList, int currentPage, int pageCount) {
        this.modelDtoList = modelDtoList;
        this.currentPage = currentPage;
        this.pageCount = pageCount;
    }
}