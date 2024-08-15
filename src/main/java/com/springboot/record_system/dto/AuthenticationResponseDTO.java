package com.springboot.record_system.dto;

import com.springboot.record_system.model.Role;

public class AuthenticationResponseDTO {
  private String token;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
