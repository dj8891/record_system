package com.springboot.record_system.dto;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;

public class RecordDTO {
  private String id;
  private String userName;
  private String company;
  private String nationality;
  private String gender;
  private String duration;
  private String clientName;
  private String ipAddress;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public LocalDateTime getCurrentDateTime() {
    // Specify the time zone for Japan
    ZoneId zoneId = ZoneId.of("Asia/Tokyo");

    // Get the current date and time in Japan
    ZonedDateTime currentDateTime = ZonedDateTime.now(zoneId);
    return currentDateTime.toLocalDateTime();
  }

  public LocalDateTime getFromDateTime() {
    // Specify the time zone for Japan
    ZoneId zoneId = ZoneId.of("Asia/Tokyo");

    // Get the current date and time in Japan
    ZonedDateTime currentDateTime = ZonedDateTime.now(zoneId);
    ZonedDateTime fromDateTime = currentDateTime.minusSeconds(Long.parseLong(this.duration));
    return fromDateTime.toLocalDateTime();
  }
}
