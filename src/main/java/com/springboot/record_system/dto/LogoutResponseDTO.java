package com.springboot.record_system.dto;

public class LogoutResponseDTO {
  private boolean isLogout;

  public boolean isLogout() {
    return isLogout;
  }

  public LogoutResponseDTO setLogout(boolean logout) {
    isLogout = logout;
    return this;
  }
}
