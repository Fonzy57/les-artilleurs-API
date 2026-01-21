package com.lesartilleursapi.auth.role.service;

import com.lesartilleursapi.auth.role.dto.RoleBaseDto;
import com.lesartilleursapi.auth.role.dto.RoleSuperAdminDto;
import com.lesartilleursapi.auth.role.model.Role;
import com.lesartilleursapi.auth.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

  private final RoleRepository roleRepository;

  @Autowired
  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  // ---------------
  // --- MAPPERS ---
  // ---------------
  private void fillBase(Role role, RoleBaseDto dto) {
    dto.setId(role.getId());
    dto.setCode(role.getCode());
    dto.setLabel(role.getLabel());
  }

  private RoleBaseDto toBaseDto(Role role) {
    RoleBaseDto dto = new RoleBaseDto();
    fillBase(role, dto);
    return dto;
  }

  private RoleSuperAdminDto toSuperAdminDto(Role role) {
    RoleSuperAdminDto dto = new RoleSuperAdminDto();
    fillBase(role, dto);
    dto.setCreatedAt(role.getCreatedAt());
    dto.setUpdatedAt(role.getUpdatedAt());
    return dto;
  }


  // -------------
  // --- ADMIN ---
  // -------------

  public List<RoleBaseDto> getAllRolesForAdmin() {
    // SAME AS => return roleRepository.findAll().stream().map(role -> this.toBaseDto(role)).toList();
    return roleRepository.findAll().stream().map(this::toBaseDto).toList();
  }

  public Optional<RoleBaseDto> getRoleByIdForAdmin(Long id) {
    return roleRepository.findById(id).map(this::toBaseDto);
  }

  // -------------------
  // --- SUPER ADMIN ---
  // -------------------

  public List<RoleSuperAdminDto> getAllRolesForSuperAdmin() {
    return roleRepository.findAll().stream().map(this::toSuperAdminDto).toList();
  }

  public Optional<RoleSuperAdminDto> getRoleByIdForSuperAdmin(Long id) {
    return roleRepository.findById(id).map(this::toSuperAdminDto);
  }
}
