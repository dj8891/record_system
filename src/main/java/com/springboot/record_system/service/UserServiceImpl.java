package com.springboot.record_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.record_system.model.User;
import com.springboot.record_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository) {
    super();
    this.userRepository = userRepository;
  }

  @Override
  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  @Override
  public Optional<User> getUserByName() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return userRepository.findByName(auth.getName());
  }

  @Override
  public User createUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @Override
  public User updateUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public void deleteUserById(String id) {
    userRepository.deleteById(id);
  }
}
