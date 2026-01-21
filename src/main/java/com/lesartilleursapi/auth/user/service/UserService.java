package com.lesartilleursapi.auth.user.service;

import com.lesartilleursapi.auth.role.dto.RoleBaseDto;
import com.lesartilleursapi.auth.role.model.Role;
import com.lesartilleursapi.auth.role.repository.RoleRepository;
import com.lesartilleursapi.auth.user.dto.*;
import com.lesartilleursapi.auth.user.model.User;
import com.lesartilleursapi.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Service layer responsible for user management business logic.
 * <p>
 * This service provides operations intended for administrative back-office usage,
 * including:
 * <ul>
 *   <li>Retrieving users (Admin and Super Admin views)</li>
 *   <li>Creating users with controlled role assignment</li>
 *   <li>Updating user profile data and password</li>
 *   <li>Deleting users (with protection for administrator accounts)</li>
 * </ul>
 * <p>
 * It also contains internal mapping utilities to convert {@link User} entities
 * into dedicated DTOs for different privilege levels.
 * <p>
 * Notes:
 * <ul>
 *   <li>This service deliberately prevents assignment or deletion of privileged roles
 *   such as {@code ADMIN} and {@code SUPER_ADMIN} from non-privileged contexts.</li>
 *   <li>HTTP-related errors are currently expressed through {@link ResponseStatusException}
 *   to propagate proper API responses.</li>
 * </ul>
 */
@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Service layer responsible for user management business logic.
   * <p>
   * This service provides operations intended for administrative back-office usage,
   * including:
   * <ul>
   *   <li>Retrieving users (Admin and Super Admin views)</li>
   *   <li>Creating users with controlled role assignment</li>
   *   <li>Updating user profile data and password</li>
   *   <li>Deleting users (with protection for administrator accounts)</li>
   * </ul>
   * <p>
   * It also contains internal mapping utilities to convert {@link User} entities
   * into dedicated DTOs for different privilege levels.
   * <p>
   * Notes:
   * <ul>
   *   <li>This service deliberately prevents assignment or deletion of privileged roles
   *   such as {@code ADMIN} and {@code SUPER_ADMIN} from non-privileged contexts.</li>
   *   <li>HTTP-related errors are currently expressed through {@link ResponseStatusException}
   *   to propagate proper API responses.</li>
   * </ul>
   */
  @Autowired
  public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // --------------
  // --- COMMON ---
  // --------------

  /**
   * Retrieves a role by its identifier and validates that it can be assigned by an administrator.
   * <p>
   * This method enforces business rules that prevent assigning privileged roles
   * (e.g. {@code ADMIN}, {@code SUPER_ADMIN}) from the current context.
   *
   * @param roleId identifier of the role to assign
   * @return the validated {@link Role}
   * @throws ResponseStatusException if the role does not exist (400) or is not assignable (403)
   */
  private Role getAssignableRoleOrThrow(Long roleId) {
    Optional<Role> userRole = roleRepository.findById(roleId);

    if (userRole.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rôle invalide");
    }

    Role role = userRole.get();

    if ("ADMIN".equals(role.getCode()) || "SUPER_ADMIN".equals(role.getCode())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'êtes pas autorisé à attribuer ce rôle.");
    }

    return role;
  }

  // ---------------
  // --- MAPPERS ---
  // ---------------

  /**
   * Maps a {@link Role} entity to a minimal {@link RoleBaseDto}.
   *
   * @param role role entity (may be null)
   * @return the mapped DTO, or {@code null} if the input is null
   */
  private RoleBaseDto toRoleBaseDto(Role role) {
    if (role == null) return null;

    RoleBaseDto dto = new RoleBaseDto();
    dto.setId(role.getId());
    dto.setCode(role.getCode());
    dto.setLabel(role.getLabel());

    return dto;
  }

  /**
   * Fills common user fields shared across multiple user DTO types.
   *
   * @param user user entity source
   * @param dto  target DTO to populate
   */
  private void fillBase(User user, UserBaseDto dto) {
    dto.setId(user.getId());
    dto.setFirstname(user.getFirstname());
    dto.setLastname(user.getLastname());
    dto.setEmail(user.getEmail());
    dto.setRole(toRoleBaseDto(user.getRole()));
  }

  /**
   * Maps a {@link User} entity to an {@link UserAdminDto}.
   *
   * @param user user entity to map
   * @return mapped admin DTO
   */
  private UserAdminDto toUserAdminDto(User user) {
    UserAdminDto dto = new UserAdminDto();
    fillBase(user, dto);
    dto.setLastLoginAt(user.getLastLoginAt());
    return dto;
  }

  /**
   * Maps a {@link User} entity to a {@link UserSuperAdminDto}.
   * <p>
   * This DTO contains additional audit fields intended for super administrator usage.
   *
   * @param user user entity to map
   * @return mapped super admin DTO
   */
  private UserSuperAdminDto toUserSuperAdminDto(User user) {
    UserSuperAdminDto dto = new UserSuperAdminDto();
    fillBase(user, dto);
    dto.setLastLoginAt(user.getLastLoginAt());
    dto.setCreatedAt(user.getCreatedAt());
    dto.setUpdatedAt(user.getUpdatedAt());
    return dto;
  }

  // -------------
  // --- ADMIN ---
  // -------------

  /**
   * Retrieves all users for the Admin view.
   * <p>
   * Uses a repository method that fetches users along with their roles to avoid N+1 queries.
   *
   * @return list of users mapped to {@link UserAdminDto}
   */
  @Transactional(readOnly = true)
  public List<UserAdminDto> getAllUsersForAdmin() {
    return userRepository.findAllWithRole().stream().map(this::toUserAdminDto).toList();
  }

  /**
   * Retrieves a single user by its identifier for the Admin view.
   *
   * @param id user identifier
   * @return an {@link Optional} containing the mapped {@link UserAdminDto} if found,
   * or {@link Optional#empty()} if not found
   */
  @Transactional(readOnly = true)
  public Optional<UserAdminDto> getUserByIdForAdmin(Long id) {
    return userRepository.findOneById(id).map(this::toUserAdminDto);
  }

  /**
   * Creates a new user.
   * <p>
   * This method enforces:
   * <ul>
   *   <li>Email uniqueness</li>
   *   <li>Role assignability (cannot assign ADMIN / SUPER_ADMIN)</li>
   *   <li>Password hashing through {@link PasswordEncoder}</li>
   * </ul>
   *
   * @param dto payload used to create the user
   * @return the created user mapped to {@link UserAdminDto}
   * @throws ResponseStatusException if the email is already used (409) or role is invalid (400/403)
   */
  @Transactional
  public UserAdminDto addUser(UserCreateDto dto) {
    Role role = getAssignableRoleOrThrow(dto.getRoleId());

    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet email est déjà utilisé.");
    }

    User newUser = new User();

    newUser.setId(null);
    newUser.setFirstname(dto.getFirstname());
    newUser.setLastname(dto.getLastname());
    newUser.setEmail(dto.getEmail());
    newUser.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
    newUser.setRole(role);

    User saved = userRepository.save(newUser);

    return toUserAdminDto(saved);
  }

  /**
   * Updates an existing user (profile fields and role).
   * <p>
   * This method enforces:
   * <ul>
   *   <li>User existence</li>
   *   <li>Email uniqueness excluding the current user</li>
   *   <li>Role assignability (cannot assign ADMIN / SUPER_ADMIN)</li>
   * </ul>
   *
   * @param id  identifier of the user to update
   * @param dto payload containing updated values
   * @return an {@link Optional} containing the updated user mapped to {@link UserAdminDto},
   * or {@link Optional#empty()} if the user does not exist
   * @throws ResponseStatusException if the email is already used (409) or role is invalid (400/403)
   */
  @Transactional
  public Optional<UserAdminDto> updateUser(Long id, UserUpdateDto dto) {
    Optional<User> existingUser = userRepository.findOneById(id);

    if (existingUser.isEmpty()) {
      return Optional.empty();
    }

    Role role = getAssignableRoleOrThrow(dto.getRoleId());

    if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet email est déjà utilisé.");
    }

    User existing = existingUser.get();
    existing.setFirstname(dto.getFirstname());
    existing.setLastname(dto.getLastname());
    existing.setEmail(dto.getEmail());
    existing.setRole(role);

    User saved = userRepository.save(existing);

    return Optional.of(toUserAdminDto(saved));
  }

  /**
   * Updates the password of an existing user.
   * <p>
   * The password is hashed using {@link PasswordEncoder} before being persisted.
   *
   * @param id  identifier of the user whose password must be updated
   * @param dto payload containing the new password
   * @throws ResponseStatusException if the user does not exist (404)
   */
  @Transactional
  public void updateUserPassword(Long id, UserPasswordUpdateDto dto) {
    Optional<User> existingUser = userRepository.findById(id);
    if (existingUser.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable");
    }

    User user = existingUser.get();
    user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));

    userRepository.save(user);
  }

  /**
   * Deletes a user by its identifier.
   * <p>
   * This method prevents deletion of privileged accounts (ADMIN / SUPER_ADMIN).
   *
   * @param id identifier of the user to delete
   * @throws ResponseStatusException if the user does not exist (404) or cannot be deleted (403)
   */
  @Transactional
  public void deleteOneUser(Long id) {
    Optional<User> user = userRepository.findOneById(id);
    if (user.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable");
    }

    String code = user.get().getRole().getCode();

    if ("ADMIN".equals(code) || "SUPER_ADMIN".equals(code)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous ne pouvez pas supprimer un compte administrateur.");
    }

    userRepository.delete(user.get());
  }


  // -------------------
  // --- SUPER ADMIN ---
  // -------------------

  /**
   * Retrieves all users for the Super Admin view.
   * <p>
   * This view contains additional audit fields compared to the Admin view.
   *
   * @return list of users mapped to {@link UserSuperAdminDto}
   */
  @Transactional(readOnly = true)
  public List<UserSuperAdminDto> getAllUsersForSuperAdmin() {
    return userRepository.findAllWithRole().stream().map(this::toUserSuperAdminDto).toList();
  }

  /**
   * Retrieves a single user by its identifier for the Super Admin view.
   *
   * @param id user identifier
   * @return an {@link Optional} containing the mapped {@link UserSuperAdminDto} if found,
   * or {@link Optional#empty()} if not found
   */
  @Transactional(readOnly = true)
  public Optional<UserSuperAdminDto> getUserByIdForSuperAdmin(Long id) {
    return userRepository.findOneById(id).map(this::toUserSuperAdminDto);
  }

  // TODO: Add later the Super Admin write operations (create/update/delete/password update).
}
