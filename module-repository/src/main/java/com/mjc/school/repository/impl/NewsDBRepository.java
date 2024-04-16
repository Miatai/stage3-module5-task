package com.mjc.school.repository.impl;

import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.filter.SearchCriteria;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NewsDBRepository extends AbstractDBRepository<News, Long> implements NewsRepository {

    @Override
    void update(News prevState, News nextState) {
        if (nextState.getTitle() != null && !nextState.getTitle().isBlank()) {
            prevState.setTitle(nextState.getTitle());
        }
        if (nextState.getContent() != null && !nextState.getContent().isBlank()) {
            prevState.setContent(nextState.getContent());
        }
        Author author = nextState.getAuthor();
        if (author != null && !author.getName().isBlank()) {
            prevState.setAuthor(nextState.getAuthor());
        }
        List<Tag> tags = nextState.getTags();
        if (tags != null && !tags.isEmpty()) {
            prevState.setTags(tags);
        }
        List<Comment> comments = nextState.getComments();
        if(comments != null && !comments.isEmpty()){
            prevState.setComments(comments);
        }
    }

    @Override
    protected void getPredicateBySearchCriteria(List<SearchCriteria> searchCriteria, CriteriaBuilder criteriaBuilder, Root<News> root, List<Predicate> predicates) {
        if(searchCriteria==null || searchCriteria.isEmpty()){
            return;
        }
        String[] classAndField;
        String rootField;
        String field;
        for (SearchCriteria criteria : searchCriteria) {
            switch (criteria.getField()) {
                case "tags.name", "tags.id", "author.name":
                    classAndField = criteria.getField().split("\\.");
                    rootField = classAndField[0].trim();
                    field = classAndField[1].trim();
                    predicates.add(criteriaBuilder.equal(root.join(rootField).get(field), criteria.getValue().toString().trim()));
                    break;
                case "title", "content":
                    predicates.add(criteriaBuilder.like(root.get(criteria.getField()), "%" + criteria.getValue().toString().trim() + "%"));
                    break;
                default:
                    throw new IllegalArgumentException("Entity " + entityClass.getName() + " does not have field " + criteria.getField() + " allowed for filtering.");
            }
        }
    }
}
