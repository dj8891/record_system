package com.springboot.record_system.dto;

import org.springframework.web.multipart.MultipartFile;

public class DetectDTO {
    private String url;
    private boolean isKeyPressed;
    private boolean isBtnClicked;
    private String ipAddress;

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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
