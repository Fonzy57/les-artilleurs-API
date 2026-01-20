package com.lesartilleursapi.auth.role.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleBaseDto {
  private Long id;
  private String code;
  private String label;
}
