package com.lesartilleursapi.site.faq.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FaqItemBaseDto {
  @NotBlank(message = "La question ne peut être vide.")
  @Size(max = 255, message = "La question ne peut pas dépasser 255 caractères.")
  private String question;

  @NotBlank(message = "La réponse ne peut être vide.")
  @Size(max = 1500, message = "La réponse ne peut pas dépasser 1500 caractères.")
  private String answer;
}
