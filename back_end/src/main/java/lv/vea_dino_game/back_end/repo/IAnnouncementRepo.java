package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.Announcement;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAnnouncementRepo extends JpaRepository<Announcement, Integer> {
    

    List<Announcement> findAllByAuthor(Player player);

    List<Announcement> findAllByClan(Clan clan);
}
