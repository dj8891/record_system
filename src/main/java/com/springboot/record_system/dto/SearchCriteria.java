package com.springboot.record_system.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SearchCriteria {
    private List<String> callers;
    private String searchTerm;
    private LocalDateTime queryTime;

    // Getters and Setters

    public List<String> getCallers() {
        return callers;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public LocalDateTime getQueryTime() {
        return queryTime;
    }
}
