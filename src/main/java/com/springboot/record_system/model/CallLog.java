package com.springboot.record_system.model;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "log")
public class CallLog {

    @Id
    private String id;
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
