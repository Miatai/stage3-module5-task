package com.mjc.school.service.validator.checker;

import com.mjc.school.service.validator.constraint.SearchCriteria;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mjc.school.service.filter.BaseSearchFilterMapper.SEARCH_DELIMITER;

@Component
public class SearchCriteriaChecker  implements ConstraintChecker<SearchCriteria>{
    @Override
    public boolean check(Object value, SearchCriteria constraint) {
        for (String criteria : (List<String>) value) {
            String[] splitCriteria = criteria.split(SEARCH_DELIMITER);
            if (splitCriteria.length < 2) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Class<SearchCriteria> getType() {
        return SearchCriteria.class;
    }
}
