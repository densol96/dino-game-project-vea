package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Combat;
import lv.vea_dino_game.back_end.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

public interface ICombatService {
    Combat attackSelectedPlayerOnArena(Integer attackerId, Integer defenderId);
}
