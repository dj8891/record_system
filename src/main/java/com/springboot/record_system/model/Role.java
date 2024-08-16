package com.springboot.record_system.model;

public enum Role {
  ADMIN,
  USER;
  public String getRoleName() {
    return "ROLE_" + name();
  }
}
