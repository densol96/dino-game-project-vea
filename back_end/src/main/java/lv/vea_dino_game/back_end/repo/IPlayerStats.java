package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.PlayerStats;
import org.springframework.data.repository.CrudRepository;

public interface IPlayerStats extends CrudRepository<PlayerStats, Integer> {
}
