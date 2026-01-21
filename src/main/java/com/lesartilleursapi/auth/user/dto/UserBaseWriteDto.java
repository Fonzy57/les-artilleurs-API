package com.lesartilleursapi.auth.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBaseWriteDto {
  @NotBlank(message = "Le prénom est obligatoire.")
  @Size(max = 100, message = "Le prénom ne peut pas dépasser 100 caractères.")
  private String firstname;

  @NotBlank(message = "Le nom de famille est obligatoire.")
  @Size(max = 100, message = "Le nom de famille ne peut pas dépasser 100 caractères.")
  private String lastname;

  @NotBlank(message = "L'email est obligatoire.")
  @Size(max = 150, message = "L'email ne peut pas dépasser 150 caractères.")
  @Email(message = "L'email n'est pas valide.")
  private String email;

  @NotNull(message = "Le rôle est obligatoire.")
  private Long roleId;
}
