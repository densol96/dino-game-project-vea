package lv.vea_dino_game.back_end.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmailDto(
  
    @NotBlank(message = "Email cannot be blank/null")
    @Size(min = 2, max = 30)
    @Email(message = "Email should be valid")
    String email
) {
  
}
