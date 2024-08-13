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
    private String ipAddress;
    private String userName;
    private String client;
    private String company;
    private String nation;
    private Boolean isVideo;
    private Boolean gender;
    private String fileLocation;
    private String caption;

    // Getters and Setters

    public LocalDateTime getFromTime() {
        return fromTime;
    }

    public LocalDateTime getToTime() {
        return toTime;
    }

    public String getIpAddress() {
        return ipAddress;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getId() {
        return this.id;
    }

    public String getNation() {
        return this.nation;
    }

    public Boolean getGender() {
        return gender;
    }

    public Boolean getIsVideo() {
        return isVideo;
    }

    public String getFileLocation() {
        return fileLocation;
    }
}
