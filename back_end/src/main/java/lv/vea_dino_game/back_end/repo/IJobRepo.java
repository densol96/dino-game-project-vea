package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IJobRepo extends JpaRepository<Job, Long> {
}
