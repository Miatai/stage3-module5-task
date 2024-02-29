package com.mjc.school.repository;

import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.repository.sorting.Sorting;
import com.mjc.school.repository.model.Comment;

import java.util.List;

public interface CommentRepository extends BaseRepository<Comment, Long> {
    Page<Comment> readByNewsId(Long newsId, Pagination pagination, List<Sorting> sorting);
}
