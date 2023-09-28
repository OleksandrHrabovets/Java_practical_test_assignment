package com.example.test_assignment.controller;

import com.example.test_assignment.model.User;
import com.example.test_assignment.model.UserDto;
import com.example.test_assignment.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<User> addUser(@Valid @RequestBody UserDto userDto) {

    return ResponseEntity.ok(userService.addUser(userDto));

  }

  @PatchMapping
  public ResponseEntity<User> updateUserField(
      @RequestParam @Email String email,
      @RequestBody Map<String, Object> update) {

    return ResponseEntity.ok(userService.updateUserFields(email, update));

  }

  @PutMapping
  public ResponseEntity<User> updateUser(@Valid @RequestBody UserDto userDto) {

    return ResponseEntity.ok(userService.updateUser(userDto));

  }

  @DeleteMapping
  public void deleteUser(@RequestParam @Email String email) {

    userService.deleteUser(email);

  }

  @GetMapping
  public List<User> searchUsersByBirthDateRange(@RequestParam LocalDate from,
      @RequestParam LocalDate to) {

    return userService.searchUsersByBirthDateRange(from, to);

  }

}
