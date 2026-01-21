package com.lesartilleursapi.auth.role.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class RoleSuperAdminDto extends RoleBaseDto {
  private Instant createdAt;
  private Instant updatedAt;
}
