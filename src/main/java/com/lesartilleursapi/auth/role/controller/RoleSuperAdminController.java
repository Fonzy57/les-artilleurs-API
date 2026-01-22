package com.lesartilleursapi.auth.role.controller;

import com.lesartilleursapi.auth.role.dto.RoleSuperAdminDto;
import com.lesartilleursapi.auth.role.service.RoleService;
import com.lesartilleursapi.auth.security.annotations.IsSuperAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("/super-admin/roles")
@IsSuperAdmin
public class RoleSuperAdminController {

  private final RoleService roleService;

  @Autowired
  public RoleSuperAdminController(RoleService roleService) {
    this.roleService = roleService;
  }

  @GetMapping
  public ResponseEntity<List<RoleSuperAdminDto>> getAllRoles() {
    List<RoleSuperAdminDto> roles = roleService.getAllRolesForSuperAdmin();
    return ResponseEntity.ok(roles);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RoleSuperAdminDto> getRoleById(@PathVariable Long id) {
    Optional<RoleSuperAdminDto> role = roleService.getRoleByIdForSuperAdmin(id);
    if (role.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(role.get());
  }
}
