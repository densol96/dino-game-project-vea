package lv.vea_dino_game.back_end.model.dto;

import lv.vea_dino_game.back_end.model.PlayerCombatsStats;
import lv.vea_dino_game.back_end.model.PlayerStats;
import lv.vea_dino_game.back_end.model.enums.DinoType;

public record PublicUserDto(
  Integer id,
  String username,
  String clanTitle,
  PlayerStats playerStats,
  DinoType dinoType,
  Integer level,
  Integer experience,
  String description,
  PlayerCombatsStats combatStats
) {
}
