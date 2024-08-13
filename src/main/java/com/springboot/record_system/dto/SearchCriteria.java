package com.springboot.record_system.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SearchCriteria {
    private List<String> callers;
    private String searchTerm;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;

    // Getters and Setters

    public List<String> getCallers() {
        return callers;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public LocalDateTime getFromTime() {
        return fromTime;
    }

    public LocalDateTime getToTime() {
        return toTime;
    }
}
