package com.springboot.record_system.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("video_log")
public class VideoLog {
    @Id
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String fileLocation;
    private String ipAddress;
    private String name;

    public VideoLog(LocalDateTime fromDate, LocalDateTime toDate, String fileLocation, String ipAddress, String name) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fileLocation = fileLocation;
        this.ipAddress = ipAddress;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
