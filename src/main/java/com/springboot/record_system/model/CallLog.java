package com.springboot.record_system.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "log")
public class CallLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private String caller;
    private String client;
    private String company;
    private Boolean isVideo;
    private String fileLocation;
    private String caption;

    // Getters and Setters

    public LocalDateTime getFromTime() {
        return fromTime;
    }

    public LocalDateTime getToTime() {
        return toTime;
    }

    public String getCaller() {
        return caller;
    }

    public String getClient() {
        return client;
    }

    public String getCompany() {
        return company;
    }

    public String getCaption() {
        return caption;
    }

    public Boolean getIsVideo() {
        return isVideo;
    }

    public String getFileLocation() {
        return fileLocation;
    }
}
