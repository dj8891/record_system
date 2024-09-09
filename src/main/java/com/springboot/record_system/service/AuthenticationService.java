package com.springboot.record_system.service;

import com.springboot.record_system.dto.AuthenticationResponseDTO;
import com.springboot.record_system.model.User;
import com.springboot.record_system.dto.UserDTO;
import com.springboot.record_system.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthenticationService(
      UserRepository userRepository,
      AuthenticationManager authenticationManager,
      PasswordEncoder passwordEncoder,
      JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public AuthenticationResponseDTO register(UserDTO request) {
    User user = new User()
        .setFirstName(request.getFirstName())
        .setLastName(request.getLastName())
        .setEmail(request.getEmail())
        .setPassword(passwordEncoder.encode(request.getPassword()))
        .setName(request.getName())
        .setRole("USER");
    userRepository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return new AuthenticationResponseDTO()
        .setToken(jwtToken);
  }

  public <Optional>User authenticate(UserDTO input) {
    try {
      Authentication auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              input.getName(),
              input.getPassword()
          )
      );
    } catch (AuthenticationException e) {
      return null;
    }
    return userRepository.findByName(input.getName())
        .orElseThrow();
  }
}