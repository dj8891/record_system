package com.springboot.record_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CallLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String caller;
    private String details;

    // Getters and Setters

    public String getCaller() {
        return caller;
    }

    public String getDetails() {
        return details;
    }
}
