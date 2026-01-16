package com.lesartilleursapi.site.infoBlock.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoBlockBaseDto {

  @Min(value = 1, message = "Il n'y a que 4 emplacement disponibles (de 1 à 4).")
  @Max(value = 4, message = "Il n'y a que 4 emplacement disponibles (de 1 à 4).")
  private Integer slot;

  @Size(max = 255, message = "Le contenu de l'info ne peut pas dépasser 255 caractères.")
  @NotBlank(message = "Le contenu du message ne peut être vide.")
  private String content;
}
