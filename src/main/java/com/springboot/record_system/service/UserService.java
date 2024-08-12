package com.springboot.record_system.service;

import com.springboot.record_system.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

  List<User> getAllUser();
  User createUser(User user);
  User updateUser(User user);
  void deleteUserById(String id);
}
