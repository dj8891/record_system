package com.springboot.record_system.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "ip_setting")
public class IPSetting {
    @Id
    private String id;

    @Indexed(unique = true)
    private String ipAddress;

    private String userName;

    public IPSetting(String id, String ipAddress, String userName) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.userName = userName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserName() {
        return userName;
    }
}
