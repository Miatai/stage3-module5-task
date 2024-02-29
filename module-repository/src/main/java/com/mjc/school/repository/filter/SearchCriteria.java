package com.mjc.school.repository.filter;

public class SearchCriteria {
//    private SearchOperation predicate;
    private String field;
//    private SearchOperation operation;
    private Object value;

    public SearchCriteria(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
