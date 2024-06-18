package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.CombatResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICombatResultRepo extends JpaRepository<CombatResult, Long> {
}
