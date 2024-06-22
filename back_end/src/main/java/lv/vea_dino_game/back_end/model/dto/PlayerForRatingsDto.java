package lv.vea_dino_game.back_end.model.dto;

import lv.vea_dino_game.back_end.model.enums.DinoType;

public record PlayerForRatingsDto(
  Integer id,
  String username,
  DinoType type,
  Integer experience,
  Integer totalFights,
  Integer fightsWon,
  Integer currencyWon
) {
}
