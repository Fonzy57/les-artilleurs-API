package com.lesartilleursapi.auth.user.dto;

import com.lesartilleursapi.auth.role.dto.RoleBaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBaseDto {
  private Long id;
  private String firstname;
  private String lastname;
  private String email;
  private RoleBaseDto role;
}
