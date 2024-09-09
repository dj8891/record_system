package com.springboot.record_system.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "detect_log")
public class DetectLog {

    @Id
    private String id;
    private String url;
    private boolean isKeyPressed;
    private boolean isBtnClicked;
    private String fileLocation;
    private Date logTime;
    private String ipAddress;

    public DetectLog() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isKeyPressed() {
        return isKeyPressed;
    }

    public void setKeyPressed(boolean keyPressed) {
        isKeyPressed = keyPressed;
    }

    public boolean isBtnClicked() {
        return isBtnClicked;
    }

    public void setBtnClicked(boolean btnClicked) {
        isBtnClicked = btnClicked;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Date  getLogTime() {
        return logTime;
    }

    public void setLogTime(Date  logTime) {
        this.logTime = logTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
