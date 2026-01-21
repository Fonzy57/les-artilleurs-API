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

@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // ---------------
  // --- MAPPERS ---
  // ---------------
  private RoleBaseDto toRoleBaseDto(Role role) {
    if (role == null) return null;

    RoleBaseDto dto = new RoleBaseDto();
    dto.setId(role.getId());
    dto.setCode(role.getCode());
    dto.setLabel(role.getLabel());

    return dto;
  }

  private void fillBase(User user, UserBaseDto dto) {
    dto.setId(user.getId());
    dto.setFirstname(user.getFirstname());
    dto.setLastname(user.getLastname());
    dto.setEmail(user.getEmail());
    dto.setRole(toRoleBaseDto(user.getRole()));
  }

  private UserAdminDto toUserAdminDto(User user) {
    UserAdminDto dto = new UserAdminDto();
    fillBase(user, dto);
    dto.setLastLoginAt(user.getLastLoginAt());
    return dto;
  }

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
  @Transactional(readOnly = true)
  public List<UserAdminDto> getAllUsersForAdmin() {
    return userRepository.findAllWithRole().stream().map(this::toUserAdminDto).toList();
  }

  @Transactional(readOnly = true)
  public Optional<UserAdminDto> getUserByIdForAdmin(Long id) {
    return userRepository.findOneById(id).map(this::toUserAdminDto);
  }

  @Transactional
  public UserAdminDto addUser(UserCreateDto dto) {
    Optional<Role> userRole = roleRepository.findById(dto.getRoleId());

    if (userRole.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rôle invalide");
    }

    Role role = userRole.get();

    if ("ADMIN".equals(role.getCode()) || "SUPER_ADMIN".equals(role.getCode())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'êtes pas autorisé à attribuer ce rôle.");
    }

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

  @Transactional
  public Optional<UserAdminDto> updateUser(Long id, UserUpdateDto dto) {
    Optional<User> existingUser = userRepository.findOneById(id);

    if (existingUser.isEmpty()) {
      return Optional.empty();
    }

    Optional<Role> userRole = roleRepository.findById(dto.getRoleId());

    if (userRole.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rôle invalide");
    }

    Role role = userRole.get();

    if ("ADMIN".equals(role.getCode()) || "SUPER_ADMIN".equals(role.getCode())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'êtes pas autorisé à attribuer ce rôle.");
    }

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

  @Transactional
  public Optional<UserAdminDto> updateUserPassword(Long id, UserPasswordUpdateDto dto) {
    Optional<User> existingUser = userRepository.findById(id);
    if (existingUser.isEmpty()) {
      return Optional.empty();
    }

    User user = existingUser.get();
    user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));

    User saved = userRepository.save(user);
    return Optional.of(toUserAdminDto(saved));
  }

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
  @Transactional(readOnly = true)
  public List<UserSuperAdminDto> getAllUsersForSuperAdmin() {
    return userRepository.findAllWithRole().stream().map(this::toUserSuperAdminDto).toList();
  }

  @Transactional(readOnly = true)
  public Optional<UserSuperAdminDto> getUserByIdForSuperAdmin(Long id) {
    return userRepository.findOneById(id).map(this::toUserSuperAdminDto);
  }

  // TODO FAIRE PLUS TARD LES MÉTHODES PUT, POST, DELETE
}
