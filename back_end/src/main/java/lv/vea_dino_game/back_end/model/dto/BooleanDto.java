package lv.vea_dino_game.back_end.model.dto;

import jakarta.validation.constraints.NotNull;

public record BooleanDto(
  @NotNull(message = "Vallue cannot be null")
  Boolean value
) {
  
}
