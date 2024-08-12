package com.springboot.record_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.record_system.dto.UserDTO;
import com.springboot.record_system.model.User;
import com.springboot.record_system.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public User updateUser(User user) {
    return userRepository.save(user);
  }

  public void deleteUserById(String id) {
    userRepository.deleteById(id);
  }
}
