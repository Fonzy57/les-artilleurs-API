package com.lesartilleursapi.auth.role.controller;

import com.lesartilleursapi.auth.role.dto.RoleBaseDto;
import com.lesartilleursapi.auth.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//  TODO METTRE ANNOTATION POUR PROTÉGER PAR ADMIN

@CrossOrigin
@RestController
@RequestMapping("/admin/roles")
public class RoleAdminController {
  private final RoleService roleService;

  @Autowired
  public RoleAdminController(RoleService roleService) {
    this.roleService = roleService;
  }

  @GetMapping
  public ResponseEntity<List<RoleBaseDto>> getAllRoles() {
    List<RoleBaseDto> roles = roleService.getAllRolesForAdmin();
    return ResponseEntity.ok(roles);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RoleBaseDto> getRoleById(@PathVariable Long id) {
    Optional<RoleBaseDto> role = roleService.getRoleByIdForAdmin(id);
    if (role.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(role.get());
  }
}
