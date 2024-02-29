package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.exception.EntityConflictRepositoryException;
import com.mjc.school.repository.pagination.Page;
import com.mjc.school.repository.pagination.Pagination;
import com.mjc.school.repository.sorting.Sorting;
import com.mjc.school.repository.sorting.SortOrder;
import com.mjc.school.repository.filter.SearchCriteria;
import com.mjc.school.repository.model.BaseEntity;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import org.hibernate.PersistentObjectException;
import org.hibernate.exception.ConstraintViolationException;

import jakarta.persistence.*;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public abstract class AbstractDBRepository<T extends BaseEntity<K>, K> implements BaseRepository<T, K> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected final Class<T> entityClass;
    private final Class<K> idClass;

    abstract void update(T prevState, T nextState);

    protected AbstractDBRepository() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        entityClass = (Class<T>) type.getActualTypeArguments()[0];
        idClass = (Class<K>) type.getActualTypeArguments()[1];
    }

    @Override
    public Page<T> readAll(Pagination pagination, List<Sorting> sorting, List<SearchCriteria> searchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        final Root<T> root = criteriaQuery.from(entityClass);

        setOrder(sorting, criteriaBuilder, criteriaQuery, root);

        List<Predicate> predicates = new ArrayList<>();

        getPredicateBySearchCriteria(searchCriteria, criteriaBuilder, root, predicates);

        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }

        final int currentPage = pagination.page();
        final int pageSize = pagination.pageSize();
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((currentPage - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return new Page<T>(typedQuery.getResultList(), currentPage, countPages(searchCriteria, pageSize));
    }

    protected abstract void getPredicateBySearchCriteria(List<SearchCriteria> searchCriteria, CriteriaBuilder criteriaBuilder, Root<T> root, List<Predicate> predicates);

    protected void setOrder(List<Sorting> sorting, CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, Root<T> root) {
        if(sorting == null || sorting.isEmpty()){
            return;
        }
        List<Order> orders = new ArrayList<>();
        for (Sorting sort : sorting) {
            Path<Object> fieldPath = root.get(sort.field());
            Order order = SortOrder.ASC.equals(sort.order()) ? criteriaBuilder.asc(fieldPath) :
                criteriaBuilder.desc(fieldPath);
            orders.add(order);
        }
        criteriaQuery.orderBy(orders);
    }

    protected int countPages(List<SearchCriteria> searchCriteria, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        final Root<T> root = countQuery.from(entityClass);

        List<Predicate> predicates = new ArrayList<>();
        getPredicateBySearchCriteria(searchCriteria, criteriaBuilder, root, predicates);
        if (!predicates.isEmpty()) {
            countQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }

        countQuery.select(criteriaBuilder.count(root));
        Long entityCount = entityManager.createQuery(countQuery).getSingleResult();

        if (entityCount % pageSize == 0) {
            return (int) (entityCount / pageSize);
        }
        return (int) (entityCount / pageSize) + 1;
    }

    @Override
    public Optional<T> readById(K id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Override
    public T create(T entity) {
        try {
            entityManager.persist(entity);
            return entity;
        } catch (PersistentObjectException | ConstraintViolationException e) {
            throw new EntityConflictRepositoryException(e.getMessage());
        }
    }

    @Override
    public T update(T entity) {
        return readById(entity.getId()).map(existingEntity -> {
            update(existingEntity, entity);
            T updated = entityManager.merge(existingEntity);
            // flush is needed for auditable entities to get actual value of @LastModifiedDate field
            entityManager.flush();
            return updated;
        }).orElse(null);
    }

    @Override
    public void deleteById(K id) {
        if (id != null) {
            T entityRef = getReference(id);
            entityManager.remove(entityRef);
        }
    }

    @Override
    public boolean existById(K id) {
        EntityType<T> entityType = entityManager.getMetamodel().entity(entityClass);
        String idFieldName = entityType.getId(idClass).getName();

        Query query = entityManager
            .createQuery("SELECT COUNT(*) FROM " + entityClass.getSimpleName() + " WHERE " + idFieldName + " = ?1")
            .setParameter(1, id);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    @Override
    public T getReference(K id) {
        return entityManager.getReference(this.entityClass, id);
    }
}
