package lv.vea_dino_game.back_end.model.dto;

import jakarta.validation.constraints.*;
import lv.vea_dino_game.back_end.model.enums.DinoType;

public record SignUpDto(

    @NotBlank(message = "Username cannot be blank/null")
    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_-]{4,14}", message = "Username must be between 5-15 characters long and can contain letters (A-Z, a-z), digits (0-9), and the special characters _ (underscore) and - (hyphen). The username must start with a letter and cannot contain any other special characters. Example: \"username123\", \"user-name\", \"user_name\".")
    String username,

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9!@#$%^&]{4,10}", message = "Password should match the pattern: [A-Za-z0-9!@#$%^&]{4,10}")
    String password,

    @NotBlank
    @Size(min = 2, max = 30)
    @Email(message = "Email should be valid")
    String email,
            
    @NotNull
    DinoType dinoType
) {
}
