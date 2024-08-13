package com.springboot.record_system.dto;

import java.util.List;

public class SearchCriteria {
    private List<String> callers;
    private String dateRange;
    private String callDuration;
    private String searchTerm;

    // Getters and Setters

    public List<String> getCallers() {
        return callers;
    }

    public String getSearchTerm() {
        return searchTerm;
    }
}
