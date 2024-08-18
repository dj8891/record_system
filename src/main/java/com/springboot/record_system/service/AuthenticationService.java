package com.springboot.record_system.service;

import com.springboot.record_system.dto.AuthenticationResponseDTO;
import com.springboot.record_system.dto.RegisterRequest;
import com.springboot.record_system.model.Role;
import com.springboot.record_system.model.User;
import com.springboot.record_system.dto.UserDTO;
import com.springboot.record_system.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthenticationService(
      UserRepository userRepository,
      AuthenticationManager authenticationManager,
      PasswordEncoder passwordEncoder,
      UserDetailsService userDetailsService,
      JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
    this.jwtService = jwtService;
  }

//  public AuthenticationResponseDTO register(RegisterRequest request) {
//    var user = User.builder()
//        .firstname(request.getFirstName())
//        .lastname(request.getLastName())
//        .email(request.getEmail())
//        .password(passwordEncoder.encode(request.getPassword()))
//        .role(Role.ADMIN)
//        .build();
//    userRepository.save(user);
//    var jwtToken = jwtService.generateToken(user);
//    return AuthenticationResponseDTO.builder()
//        .token(jwtToken)
//        .build();
//  }

  public User authenticate(UserDTO input, HttpServletRequest request) {
//    try {
//      String pass = input.getPassword();
//      String name = input.getName();
//      Authentication auth = authenticationManager.authenticate(
//          new UsernamePasswordAuthenticationToken(
//              input.getName(),
//              input.getPassword()
//          )
//      );
//    } catch (AuthenticationException e) {
//      System.out.println(e.getMessage());
//    }
//    return userRepository.findByName(input.getName())
//        .orElseThrow();
    User user = userRepository.findByName(input.getName()).orElseThrow(() -> new UsernameNotFoundException("UserName not found"));
    if(passwordEncoder.matches(input.getPassword(), user.getPassword()))
    {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(input.getName());
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authToken);
      return user;
    }
    else
      return null ;
  }
}