package lv.vea_dino_game.back_end.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MailDto(

  @NotBlank(message = "Mail message must have a to-user's username")
  @Pattern(regexp = "[A-Za-z][A-Za-z0-9_-]{4,14}", message = "To username must be between 5-15 characters long and can contain letters (A-Z, a-z), digits (0-9), and the special characters _ (underscore) and - (hyphen). The username must start with a letter and cannot contain any other special characters. Example: 'username123', 'user-name', 'user_name'.")
  String to,
          
  @NotBlank(message = "Mail message title cannot be blank/null")
  @Size(max = 40, message = "Mail message title cannot be longer than 40 characters")
  String title,
          
  @NotBlank(message = "Mail message cannot be blank/null")
  @Size(max = 400, message = "Mail message cannot be longer than 400 characters")
  String messageText
) {

}
