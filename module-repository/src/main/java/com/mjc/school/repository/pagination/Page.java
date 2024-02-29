package com.mjc.school.repository.pagination;

import java.util.List;

public record Page<T>(List<T> entities, int currentPage, int pageCount) {
}
