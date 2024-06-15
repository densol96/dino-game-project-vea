package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Combat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICombatRepo  extends JpaRepository<Combat, Long> {

}
