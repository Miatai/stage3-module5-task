package com.mjc.school.repository.model;

import javax.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "news")
@EntityListeners(AuditingEntityListener.class)
public class News implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "newstags",
        joinColumns = { @JoinColumn(name = "news_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    private List<Tag> tags;

    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY)
    private List<Comment> comments;

    public News() {
    }

    public News(
            Long id,
            String title,
            String content,
            LocalDateTime createdDate,
            LocalDateTime lastUpdatedDate,
            Author author, List<Tag> tags,
            List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.author = author;
        this.tags = tags;
        this.comments = comments;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createDate) {
        this.createdDate = createDate;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(final LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(id, news.id)
                && Objects.equals(title, news.title)
                && Objects.equals(content, news.content)
                && Objects.equals(createdDate, news.createdDate)
                && Objects.equals(lastUpdatedDate, news.lastUpdatedDate)
                && Objects.equals(author, news.author)
                && Objects.equals(tags, news.tags)
                && Objects.equals(comments, news.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, createdDate, lastUpdatedDate, author, tags, comments);
    }
}
