package com.mjc.school.repository.query;

import com.mjc.school.repository.model.Author;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AuthorWithNewsCount {
    private Author author;
    private Long newsCount;
}
