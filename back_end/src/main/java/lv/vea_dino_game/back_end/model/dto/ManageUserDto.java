package lv.vea_dino_game.back_end.model.dto;

import java.time.LocalDateTime;

public record ManageUserDto(
  Integer id,
  String description,
  String email,
  String username,
  LocalDateTime registrationDate,
  LocalDateTime lastLoggedIn,
  Boolean isEmailConfirmed,
  LocalDateTime tempBanDateTime,
  Boolean accountDisabled
) {
  
}
