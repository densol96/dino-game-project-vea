package lv.vea_dino_game.back_end.model.dto;

import jakarta.validation.constraints.*;

public record SignInDto(
  
  @NotBlank(message = "Username cannot be blank/null")
  @Pattern(regexp = "[A-Za-z][A-Za-z0-9_-]{4,14}", message = "Username must be between 5-15 characters long and can contain letters (A-Z, a-z), digits (0-9), and the special characters _ (underscore) and - (hyphen). The username must start with a letter and cannot contain any other special characters. Example: \"username123\", \"user-name\", \"user_name\".")
  String username,
          
  @NotBlank
  @Size(min = 2, max = 30, message = "Password length must be between 2 and 30 characters")
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Password requires at least 8 characters, with at least one uppercase letter and one number")
  String password
) {
  
}
