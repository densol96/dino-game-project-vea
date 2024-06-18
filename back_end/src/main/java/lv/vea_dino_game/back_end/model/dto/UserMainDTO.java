package lv.vea_dino_game.back_end.model.dto;

import lv.vea_dino_game.back_end.model.PlayerStats;
import lv.vea_dino_game.back_end.model.enums.DinoType;

public record UserMainDTO(
  Integer id,
  String username,
  String email,
  String clanTitle,
  PlayerStats playerStats,
  DinoType dinoType,
  Integer level,
  Integer experience,
  Integer currency,
  String description
) {
}
