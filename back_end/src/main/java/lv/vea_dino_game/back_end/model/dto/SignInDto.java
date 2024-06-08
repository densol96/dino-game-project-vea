package lv.vea_dino_game.back_end.model.dto;

import jakarta.validation.constraints.*;

public record SignInDto(
  @NotBlank
  @Size(min = 2, max = 30, message = "Username length must be between 2 and 30 characters")
  @Pattern(regexp = "[A-Z]{1}[a-z]+", message = "First username character must be upper-case and other can be lower-case characters")
  String username,
          
  @NotBlank
  @Size(min = 2, max = 30, message = "Password length must be between 2 and 30 characters")
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Password requires at least 8 characters, with at least one uppercase letter and one number")
  String password
) {
  
}
