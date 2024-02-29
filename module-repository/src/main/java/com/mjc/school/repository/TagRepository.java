package com.mjc.school.repository;

import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.repository.sorting.Sorting;
import com.mjc.school.repository.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends BaseRepository<Tag, Long> {
    Page<Tag> readByNewsId(Long newsId, Pagination pagination, List<Sorting> sorting);
    Optional<Tag> readByName(String name);
}
