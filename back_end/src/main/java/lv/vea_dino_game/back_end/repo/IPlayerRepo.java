package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.Player;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlayerRepo extends JpaRepository<Player, Integer> {
}
