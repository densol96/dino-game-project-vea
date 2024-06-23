package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Combat;
import lv.vea_dino_game.back_end.model.dto.ArenaSearchPlayerDto;
import lv.vea_dino_game.back_end.model.dto.CombatResultDto;







public interface ICombatService {
  CombatResultDto attackSelectedPlayerOnArena(Integer defenderId);

  ArenaSearchPlayerDto getRandomPlayerThatCanBeAttackedRn();
}
