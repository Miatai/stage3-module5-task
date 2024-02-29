package com.mjc.school.repository.impl;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.filter.SearchCriteria;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.repository.query.AuthorWithNewsCount;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorDBRepository extends AbstractDBRepository<Author, Long> implements AuthorRepository {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    void update(Author prevState, Author nextState) {
        if (nextState.getName() != null && !nextState.getName().isBlank()) {
            prevState.setName(nextState.getName());
        }
    }

    @Override
    protected void getPredicateBySearchCriteria(List<SearchCriteria> searchCriteria, CriteriaBuilder criteriaBuilder, Root<Author> root, List<Predicate> predicates) {

    }

    @Override
    public Optional<Author> readByNewsId(Long newsId) {
        TypedQuery<Author> typedQuery = entityManager
            .createQuery("SELECT a FROM Author a INNER JOIN a.news n WHERE n.id=:newsId", Author.class)
            .setParameter("newsId", newsId);
        try {
            return Optional.of(typedQuery.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Author> readByName(String name) {
        TypedQuery<Author> typedQuery = entityManager
                .createQuery("SELECT a FROM Author a WHERE a.name = :authorName", Author.class)
                .setParameter("authorName", name)
                .setMaxResults(1);
        try {
            return Optional.of(typedQuery.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Page<AuthorWithNewsCount> readWithNewsCount(Pagination pagination) {
        List<AuthorWithNewsCount> results = new ArrayList<>();
        final int currentPage = pagination.page();
        final int pageSize = pagination.pageSize();
        Query query = entityManager
            .createQuery("SELECT a, COUNT (n) AS newsCount FROM Author a LEFT JOIN a.news n GROUP BY a.id ORDER BY newsCount DESC");
        query.setFirstResult((currentPage - 1) * pageSize);
        query.setMaxResults(pageSize);
        List x = query.getResultList();
        for(Object o : x) {
            Object[] y = (Object[])o;
            results.add(new AuthorWithNewsCount((Author) y[0], (Long) y[1]));
        }
        return new Page<>(results, currentPage, countPages(null, pageSize));
    }
}
