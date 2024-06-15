package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.PlayerStats;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlayerStats extends JpaRepository<PlayerStats, Integer> {
}
