package com.lesartilleursapi.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDto {
  private String accessToken;
  private String tokenType;
}