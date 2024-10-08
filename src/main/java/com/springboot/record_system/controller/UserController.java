package com.springboot.record_system.controller;

import com.springboot.record_system.dto.PasswordResetRequest;
import com.springboot.record_system.model.User;
import com.springboot.record_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(path = "/users")
  public List<User> getAllUsers() {
    return userService.getAllUser();
  }

  @GetMapping(path = "/user")
  public ResponseEntity<User> getUserByName() {
    return ResponseEntity.ok(userService.getUserByName().orElseThrow());
  }

  @PostMapping(path = "/users")
  public User createUser(@RequestBody User user) {
    return userService.createUser(user);
  }

  @PutMapping(path = "/users/{id}")
  public User udpateUser(@RequestBody User user, @PathVariable(name="id") String id) {
    user.setId(id);
    userService.updateUser(user);
    return user;
  }

  @DeleteMapping(path = "/users/{id}")
  public void deleteUserById(@PathVariable String id) {
    userService.deleteUserById(id);
  }

  @PostMapping(path = "/reset")
  public ResponseEntity<?> reset(@RequestBody PasswordResetRequest resetRequest) {
    return userService.resetPassword(resetRequest);
  }
}
