package com.lesartilleursapi.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshRequestDto {

  @NotBlank
  private String refreshToken;

  private boolean rememberMe;
}
