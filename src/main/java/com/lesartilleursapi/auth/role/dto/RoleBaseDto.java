package com.lesartilleursapi.auth.role.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleBaseDto {

  private Long id;

  @NotBlank(message = "Le code du role ne peut être vide.")
  @Size(max = 50, message = "Le code du role ne peut pas dépasser 50 caractères.")
  private String code;

  @NotBlank(message = "Le label du role ne peut être vide.")
  @Size(max = 100, message = "Le label du role ne peut pas dépasser 100 caractères.")
  private String label;
}
