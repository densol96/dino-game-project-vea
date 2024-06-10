package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.Clan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface IClanRepo extends JpaRepository<Clan, Long> {

    ArrayList<Clan> findAllByMinPlayerLevelGreaterThanEqual(int level);

    Clan findById(int id);

    @Query("SELECT el FROM Clan el ORDER BY el.minPlayerLevel DESC")
    List<Clan> findAllSortedByMinPlayerLevelDesc();

    @Query("SELECT el FROM Clan el ORDER BY el.minPlayerLevel ASC")
    List<Clan> findAllSortedByMinPlayerLevelAsc();
}
