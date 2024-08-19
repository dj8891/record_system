package com.springboot.record_system.controller;

import com.springboot.record_system.dto.AuthenticationResponseDTO;
import com.springboot.record_system.dto.LoginResponseDTO;
import com.springboot.record_system.dto.LogoutResponseDTO;
import com.springboot.record_system.dto.UserDTO;
import com.springboot.record_system.model.User;
import com.springboot.record_system.service.JwtService;
import com.springboot.record_system.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/auth")
public class LoginController {
  private final JwtService jwtService;

  private final AuthenticationService authenticationService;
  private final AuthenticationConfiguration authenticationConfiguration;

  public LoginController(JwtService jwtService, AuthenticationService authenticationService, AuthenticationConfiguration authenticationConfiguration) {
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
    this.authenticationConfiguration = authenticationConfiguration;
  }

  @PostMapping(path = "/register")
  public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody UserDTO userDTO) {
    AuthenticationResponseDTO authenticationResponseDTO = authenticationService.register(userDTO);
    return ResponseEntity.ok(authenticationResponseDTO);
  }

  @PostMapping(path = "/login")
  public ResponseEntity<LoginResponseDTO> login(@RequestBody UserDTO loginUserDto) {
    User authenticatedUser = authenticationService.authenticate(loginUserDto);
    if(authenticatedUser != null) {
      String jwtToken = jwtService.generateToken(authenticatedUser);
      LoginResponseDTO loginResponse = new LoginResponseDTO().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());
      return ResponseEntity.ok(loginResponse);
    } else {
      LoginResponseDTO loginResponseDTO = new LoginResponseDTO().setPasswordNotMatched("Password Not Matched");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(loginResponseDTO);
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<LogoutResponseDTO> logout() {
    SecurityContextHolder.getContext().setAuthentication(null);
    LogoutResponseDTO logoutResponseDTO = new LogoutResponseDTO().setLogout(true);
    return ResponseEntity.ok(logoutResponseDTO);
  }
}
