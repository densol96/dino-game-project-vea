package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.Player;
import org.springframework.data.repository.CrudRepository;

public interface IPlayerRepo extends CrudRepository<Player, Integer> {
}
