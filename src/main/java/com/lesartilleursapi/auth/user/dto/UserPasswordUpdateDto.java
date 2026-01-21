package com.lesartilleursapi.auth.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordUpdateDto {
  @NotBlank(message = "Le mot de passe est obligatoire.")
  @Size(min = 14, max = 40, message = "Le mot de passe doit contenir entre 14 et 40 caractères.")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{14,40}$",
      message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial."
  )
  private String newPassword;
}

// TODO FAIRE UN CURRENT PASSWORD + NEW PASSWORD PLUS TARD
