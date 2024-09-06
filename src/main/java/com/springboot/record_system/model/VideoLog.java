package com.springboot.record_system.model;

import org.jcodec.common.DictionaryCompressor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("video_log")
public class VideoLog {
    @Id
    private String id;
    private Date fromDate;
    private Date toDate;
    private String fileLocation;
    private String ipAddress;
    private String name;
    private List<String> urls;
    private int keyCount;
    private int mouseCount;
    private Integer duration;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public VideoLog(Date fromDate, Date toDate, String fileLocation, String ipAddress, String name, Integer duration) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fileLocation = fileLocation;
        this.ipAddress = ipAddress;
        this.name = name;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
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

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public int getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(int keyCount) {
        this.keyCount = keyCount;
    }

    public int getMouseCount() {
        return mouseCount;
    }

    public void setMouseCount(int mouseCount) {
        this.mouseCount = mouseCount;
    }
}
