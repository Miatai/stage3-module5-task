package com.mjc.school.repository;

import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.query.AuthorWithNewsCount;

import java.util.Optional;

public interface AuthorRepository extends BaseRepository<Author, Long> {
    Optional<Author> readByNewsId(Long newsId);
    Optional<Author> readByName(String name);
    Page<AuthorWithNewsCount> readWithNewsCount(Pagination pagination);
}
