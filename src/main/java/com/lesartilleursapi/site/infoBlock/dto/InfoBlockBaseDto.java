package com.lesartilleursapi.site.infoBlock.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoBlockBaseDto {

  @Min(1)
  @Max(4)
  private Integer slot;

  @NotBlank(message = "Le contenu du message ne peut être vide.")
  private String content;
}
