package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.Player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPlayerRepo extends JpaRepository<Player, Integer> {

    List<Player> findAllByOrderByLevelDesc();

    List<Player> findAllByOrderByLevelAsc();
}
