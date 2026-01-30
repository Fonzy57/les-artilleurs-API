package com.lesartilleursapi.auth.user.controller;

import com.lesartilleursapi.auth.security.annotations.IsAdmin;
import com.lesartilleursapi.auth.security.annotations.IsSuperAdmin;
import com.lesartilleursapi.auth.user.dto.UserAdminDto;
import com.lesartilleursapi.auth.user.dto.UserCreateDto;
import com.lesartilleursapi.auth.user.dto.UserPasswordUpdateDto;
import com.lesartilleursapi.auth.user.dto.UserUpdateDto;
import com.lesartilleursapi.auth.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Administrative REST controller for user management.
 * <p>
 * This controller exposes endpoints intended for administrators to manage user accounts:
 * <ul>
 *   <li>List users</li>
 *   <li>Retrieve a single user</li>
 *   <li>Create a user</li>
 *   <li>Update a user</li>
 *   <li>Delete a user</li>
 *   <li>Update a user's password</li>
 * </ul>
 * <p>
 * All endpoints under this controller are intended for administrative usage and
 * should be protected by authentication and authorization mechanisms (e.g. ADMIN role).
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/users")
@IsAdmin
public class UserAdminController {

  private final UserService userService;

  /**
   * Constructs a new {@link UserAdminController}.
   *
   * @param userService service responsible for user management business logic
   */
  @Autowired
  public UserAdminController(UserService userService) {
    this.userService = userService;
  }

  // TODO FAIRE DES ROUTES /me/infos POUR LE COMPTE, VOIR SI JE FAIS UN AUTRE CONTROLLER OU SI JE GARDE CELUI LA
  // PAR EXEMPLE POUR LE CHANGEMENT DE MOT DE PASSE OU D'EMAIL

  /**
   * Retrieves all users for the admin view.
   *
   * @return a {@link ResponseEntity} containing the list of users.
   * Returns an empty list if no users are found.
   */
  @GetMapping
  public ResponseEntity<List<UserAdminDto>> getAllUsers() {
    List<UserAdminDto> users = userService.getAllUsersForAdmin();
    return ResponseEntity.ok(users);
  }

  /**
   * Retrieves a single user by its identifier.
   *
   * @param id the identifier of the user
   * @return a {@link ResponseEntity} containing the user if found,
   * or {@code 404 Not Found} if the user does not exist
   */
  @GetMapping("/{id}")
  public ResponseEntity<UserAdminDto> getOneUser(@PathVariable Long id) {
    Optional<UserAdminDto> user = userService.getUserByIdForAdmin(id);
    if (user.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(user.get());
  }

  /**
   * Creates a new user.
   * <p>
   * The request body is validated using Jakarta Bean Validation annotations.
   *
   * @param user payload used to create a new user
   * @return a {@link ResponseEntity} containing the created user with HTTP status {@code 201 Created}
   */
  @PostMapping
  public ResponseEntity<UserAdminDto> addUser(@RequestBody @Valid UserCreateDto user) {
    UserAdminDto createdUser = userService.addUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  /**
   * Updates an existing user by its identifier.
   * <p>
   * The request body is validated using Jakarta Bean Validation annotations.
   *
   * @param id  the identifier of the user to update
   * @param dto payload containing updated user data
   * @return a {@link ResponseEntity} containing the updated user if found,
   * or {@code 404 Not Found} if the user does not exist
   */
  @PutMapping("/{id}")
  public ResponseEntity<UserAdminDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDto dto) {
    Optional<UserAdminDto> updatedUser = userService.updateUser(id, dto);
    if (updatedUser.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(updatedUser.get());
  }

  /**
   * Deletes a user by its identifier.
   *
   * @param id the identifier of the user to delete
   * @return {@code 204 No Content} if the deletion was successful
   */
  @IsSuperAdmin
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteOneUser(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Updates a user's password.
   * <p>
   * This endpoint is intended for administrative password resets.
   * The request body is validated using Jakarta Bean Validation annotations.
   *
   * @param id  the identifier of the user whose password must be updated
   * @param dto payload containing the new password
   * @return {@code 204 No Content} if the password update was successful
   */
  @PatchMapping("/{id}/password")
  public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody @Valid UserPasswordUpdateDto dto) {
    userService.updateUserPassword(id, dto);
    return ResponseEntity.noContent().build();
  }

  // TODO: Later implement PATCH /api/me/password for the connected user,
  // requiring the current password before setting a new one.
}
