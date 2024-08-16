package com.springboot.record_system.controller;

import com.springboot.record_system.dto.PasswordResetRequest;
import com.springboot.record_system.model.CallLog;
import com.springboot.record_system.model.User;
import com.springboot.record_system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Optional<User> optional = userService.findByName(auth.getName());

    User user = optional.orElse(null);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is unauthenticated");
    } else {
      if (!Objects.equals(user.getPassword(), resetRequest.getCurrentPassword())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect.");
      }
      user.setPassword(resetRequest.getNewPassword());
      userService.updateUser(user);
      return ResponseEntity.status(HttpStatus.OK).body("Password is successfully reset");
    }
  }
}
