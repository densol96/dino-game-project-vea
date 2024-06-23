package lv.vea_dino_game.back_end.model.dto;

import lv.vea_dino_game.back_end.model.PlayerStats;

public record ArenaSearchPlayerDto(
  Integer id,
  String username,
  PlayerStats playerStats,
  Integer level
) {
  
}
