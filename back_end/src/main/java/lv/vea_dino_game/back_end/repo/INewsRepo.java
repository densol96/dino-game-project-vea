package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INewsRepo extends JpaRepository<News, Integer> {
}
