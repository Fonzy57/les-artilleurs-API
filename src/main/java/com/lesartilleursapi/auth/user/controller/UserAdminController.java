package com.lesartilleursapi.auth.user.controller;

import com.lesartilleursapi.auth.user.dto.UserAdminDto;
import com.lesartilleursapi.auth.user.dto.UserCreateDto;
import com.lesartilleursapi.auth.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/users")
public class UserAdminController {

  private final UserService userService;

  @Autowired
  public UserAdminController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<UserAdminDto>> getAllUsers() {
    List<UserAdminDto> users = userService.getAllUsersForAdmin();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserAdminDto> getOneUser(@PathVariable Long id) {
    Optional<UserAdminDto> user = userService.getUserByIdForAdmin(id);
    if (user.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(user.get());
  }

  @PostMapping
  public ResponseEntity<UserAdminDto> addUser(@RequestBody @Valid UserCreateDto user) {
    UserAdminDto createdUser = userService.addUser(user);
    return ResponseEntity.status(201).body(createdUser);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteOneUser(id);
    return ResponseEntity.noContent().build();
  }
}
