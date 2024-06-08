package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.Clan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface IClanRepo extends JpaRepository<Clan, Long> {

    ArrayList<Clan> findAllByMinPlayerLevelGreaterThanEqual(int level);
}
