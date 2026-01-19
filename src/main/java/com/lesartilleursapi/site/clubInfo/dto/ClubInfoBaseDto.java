package com.lesartilleursapi.site.clubInfo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubInfoBaseDto {

  @NotBlank(message = "Le nom du club est obligatoire")
  @Size(max = 100, message = "Le nom du club ne peut pas dépasser 100 caractères")
  private String clubName;

  @NotBlank(message = "Le nom du stade est obligatoire")
  @Size(max = 100, message = "Le nom du stade ne peut pas dépasser 100 caractères")
  private String stadiumName;

  @NotBlank(message = "Le nom de la rue est obligatoire")
  @Size(max = 150, message = "Le nom de la rue ne peut pas dépasser 150 caractères")
  private String street;

  @NotBlank(message = "Le nom de la ville est obligatoire")
  @Size(max = 80, message = "Le nom de la ville ne peut pas dépasser 80 caractères")
  private String city;

  @NotBlank(message = "Le code postal est obligatoire")
  @Size(max = 5, message = "Le code postal ne peut pas dépasser 5 caractères")
  private String postalCode;

  @NotBlank(message = "L'adresse email de contact est obligatoire")
  @Email(message = "L'adresse email de contact n'est pas valide")
  @Size(max = 150, message = "L'adresse mail de contact ne peut pas dépasser 150 caractères")
  private String contactEmail;
}
