package com.mjc.school.repository.impl;

import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.filter.SearchCriteria;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TagDBRepository extends AbstractDBRepository<Tag, Long> implements TagRepository {

    @Override
    void update(Tag prevState, Tag nextState) {
        if (nextState.getName() != null && !nextState.getName().isBlank()) {
            prevState.setName(nextState.getName());
        }
    }

    @Override
    protected void getPredicateBySearchCriteria(List<SearchCriteria> searchCriteria, CriteriaBuilder criteriaBuilder, Root<Tag> root, List<Predicate> predicates) {
        if(searchCriteria==null || searchCriteria.isEmpty()){
            return;
        }
        for (SearchCriteria criteria : searchCriteria) {
            if(criteria.getField().equals("name")){
                predicates.add(criteriaBuilder.like(root.get(criteria.getField()), "%" + criteria.getValue().toString().trim() + "%"));
            }
        }
    }

    @Override
    public Page<Tag> readByNewsId(Long newsId, Pagination pagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        final Root<Tag> root = criteriaQuery.from(Tag.class);
        Join<News, Tag> join = root.join("news");

        criteriaQuery.select(root).where(criteriaBuilder.equal(join.get("id"), newsId));

        final int currentPage = pagination.page();
        final int pageSize = pagination.pageSize();
        TypedQuery<Tag> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((currentPage - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);
        return new Page<>(typedQuery.getResultList(),currentPage, countPages(null, pageSize));
    }

    @Override
    public Optional<Tag> readByName(String name) {
        TypedQuery<Tag> typedQuery = entityManager
                .createQuery("SELECT t FROM Tag t WHERE t.name = :tagName", Tag.class)
                .setParameter("tagName", name)
                .setMaxResults(1);
        try {
            return Optional.of(typedQuery.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
