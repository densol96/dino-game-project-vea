package lv.vea_dino_game.back_end.model.dto;

import lv.vea_dino_game.back_end.model.enums.EnumCombatResultType;

public record CombatResultDto(
  
  String winner,
  String loser,
  EnumCombatResultType combatResultType,
  Integer winnerCurrencyChange,
  Integer loserCurrencyChange,
  Integer winnerExpReward,
  Integer loserExpReward
) {
  
}
