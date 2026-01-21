package com.lesartilleursapi.auth.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserAdminDto extends UserBaseDto {
  private Instant lastLoginAt;
}
