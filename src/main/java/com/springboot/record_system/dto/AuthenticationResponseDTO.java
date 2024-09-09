package com.springboot.record_system.dto;

public class AuthenticationResponseDTO {
  private String token;

  public String getToken() {
    return token;
  }

  public AuthenticationResponseDTO setToken(String token) {
    this.token = token;
    return this;
  }
}
