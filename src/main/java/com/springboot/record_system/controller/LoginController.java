package com.springboot.record_system.controller;

import com.springboot.record_system.dto.AuthenticationResponseDTO;
import com.springboot.record_system.dto.LoginResponseDTO;
import com.springboot.record_system.dto.LogoutResponseDTO;
import com.springboot.record_system.dto.UserDTO;
import com.springboot.record_system.model.User;
import com.springboot.record_system.service.JwtService;
import com.springboot.record_system.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/auth")
public class LoginController {
  private final JwtService jwtService;

  private final AuthenticationService authenticationService;

  public LoginController(JwtService jwtService, AuthenticationService authenticationService) {
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
  }

  @PostMapping(path = "/login")
  public ResponseEntity<LoginResponseDTO> login(@RequestBody UserDTO loginUserDto) {

    User authenticatedUser = authenticationService.authenticate(loginUserDto);

    String jwtToken = jwtService.generateToken(authenticatedUser);

    LoginResponseDTO loginResponse = new LoginResponseDTO().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

    return ResponseEntity.ok(loginResponse);
  }

  @PostMapping("/logout")
  public ResponseEntity<LogoutResponseDTO> logout() {
    SecurityContextHolder.getContext().setAuthentication(null);
    LogoutResponseDTO logoutResponseDTO = new LogoutResponseDTO().setLogout(true);
    return ResponseEntity.ok(logoutResponseDTO);
  }
}
