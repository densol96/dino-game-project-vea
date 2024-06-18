package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.CombatHistoryRecap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICombatHistoryRecapRepo extends JpaRepository<CombatHistoryRecap, Long> {
}
