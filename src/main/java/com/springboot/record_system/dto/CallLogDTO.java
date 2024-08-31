package com.springboot.record_system.dto;

import java.time.LocalDateTime;

public class CallLogDTO {
    private String id;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private String ipAddress;
    private String userName;
    private String client;
    private String company;
    private Boolean isVideo;
    private Boolean gender;
    private String fileLocation;
    private String caption;
    private String nationality;
}
