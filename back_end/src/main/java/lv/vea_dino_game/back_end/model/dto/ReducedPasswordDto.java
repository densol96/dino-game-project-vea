package lv.vea_dino_game.back_end.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ReducedPasswordDto(
            
  @NotBlank(message = "Password cannot be blank/null")
  @Pattern(regexp = "[A-Za-z0-9!@#$%^&]{4,20}", message = "Password should match the pattern: [A-Za-z0-9!@#$%^&]{4,20}")
  String password
) {
  
}