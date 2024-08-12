package com.springboot.record_system.service;

import com.springboot.record_system.model.User;
import com.springboot.record_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

  @Autowired
  private UserRepository userRepository;

  public List<User> getAllUser() {
    List<User> user = userRepository.findAll();
    return user;
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
