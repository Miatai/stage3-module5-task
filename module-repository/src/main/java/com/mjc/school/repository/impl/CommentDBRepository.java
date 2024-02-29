package com.mjc.school.repository.impl;

import com.mjc.school.repository.CommentRepository;
import com.mjc.school.repository.filter.SearchCriteria;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.repository.sorting.Sorting;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentDBRepository extends AbstractDBRepository<Comment, Long> implements CommentRepository {

    @Override
    public Page<Comment> readByNewsId(Long newsId, Pagination pagination, List<Sorting> sorting) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
        final Root<Comment> root = criteriaQuery.from(Comment.class);
        Join<News, Comment> join = root.join("news");

        criteriaQuery.select(root).where(criteriaBuilder.equal(join.get("id"), newsId));

        setOrder(sorting, criteriaBuilder, criteriaQuery, root);

        final int currentPage = pagination.page();
        final int pageSize = pagination.pageSize();
        TypedQuery<Comment> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((currentPage - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);
        return new Page<>(typedQuery.getResultList(), currentPage, countPages(null, pageSize));
    }

    @Override
    void update(Comment prevState, Comment nextState) {
        if (nextState.getContent() != null && !nextState.getContent().isBlank()) {
            prevState.setContent(nextState.getContent());
        }
        News news = nextState.getNews();
        if (news != null && !news.getTitle().isBlank()) {
            prevState.setNews(nextState.getNews());
        }
    }

    @Override
    protected void getPredicateBySearchCriteria(List<SearchCriteria> searchCriteria, CriteriaBuilder criteriaBuilder, Root<Comment> root, List<Predicate> predicates) {

    }
}
