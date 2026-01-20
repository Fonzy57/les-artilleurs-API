package com.lesartilleursapi.auth.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserSuperAdminDto extends UserAdminDto {
  private Instant createdAt;
  private Instant updatedAt;
}
