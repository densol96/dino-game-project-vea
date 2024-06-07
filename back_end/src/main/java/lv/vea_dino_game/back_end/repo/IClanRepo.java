package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.Clan;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface IClanRepo extends CrudRepository<Clan, Long> {

    ArrayList<Clan> findAllByMinPlayerLevelGreaterThanEqual(int level);
}
