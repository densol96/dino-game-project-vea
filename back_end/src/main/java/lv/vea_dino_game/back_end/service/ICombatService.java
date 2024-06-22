package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Combat;







public interface ICombatService {
    Combat attackSelectedPlayerOnArena(Integer attackerId, Integer defenderId);
}
