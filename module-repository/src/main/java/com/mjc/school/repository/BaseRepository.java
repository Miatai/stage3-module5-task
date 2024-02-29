package com.mjc.school.repository;

import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.repository.sorting.Sorting;
import com.mjc.school.repository.filter.SearchCriteria;
import com.mjc.school.repository.model.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T extends BaseEntity<K>, K> {

    Page<T> readAll(Pagination pagination, List<Sorting> sorting, List<SearchCriteria> searchCriteria);

    Optional<T> readById(K id);

    T create(T entity);

    T update(T entity);

    void deleteById(K id);

    boolean existById(K id);

    T getReference(K id);
}
