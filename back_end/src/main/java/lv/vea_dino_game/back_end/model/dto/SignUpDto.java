package lv.vea_dino_game.back_end.model.dto;

import jakarta.validation.constraints.*;
import lv.vea_dino_game.back_end.model.enums.DinoType;

public record SignUpDto(
    @NotBlank
    @Size(min = 2, max = 30, message = "Username length must be between 2 and 30 characters")
    @Pattern(regexp = "[A-Z]{1}[a-z]+", message = "First username character must be upper-case and other can be lower-case characters")
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
