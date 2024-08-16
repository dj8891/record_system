package com.springboot.record_system.service;

import com.springboot.record_system.model.User;
import com.springboot.record_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByName(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    // Return a UserDetails object
    return new org.springframework.security.core.userdetails.User(
        user.getName(),
        user.getPassword(),
        user.getAuthorities() // Ensure this returns a collection of GrantedAuthority
    );
  }
}
