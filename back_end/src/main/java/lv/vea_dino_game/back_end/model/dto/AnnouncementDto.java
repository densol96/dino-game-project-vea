package lv.vea_dino_game.back_end.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnnouncementDto(
    @NotBlank(message = "Title cannot be blank/null")
    @Size(min = 4, max = 50, message = "The title must be minimum 4 characters and maximum 50 characters")
    String title,

    @NotBlank(message = "Content cannot be blank/null")
    @Size(min = 4, max = 50, message = "The content must be minimum 4 characters and maximum 50 characters")
    String content

){
}
