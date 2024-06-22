package lv.vea_dino_game.back_end.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record BanWithTimeDto(
        @NotBlank(message = "Title cannot be blank/null")
        @Size(min = 4, max = 50, message = "The title must be minimum 4 characters and maximum 50 characters")
        String title,

        @NotBlank(message = "Message cannot be blank/null")
        @Size(min = 4, max = 50, message = "The message must be minimum 4 characters and maximum 50 characters")
        String message,

        @NotNull(message = "Registration date-time should be added upon persisting user model to DB")
        LocalDateTime date
) {
}
