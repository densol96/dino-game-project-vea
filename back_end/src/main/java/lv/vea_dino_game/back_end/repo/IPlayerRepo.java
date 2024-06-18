package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IPlayerRepo extends JpaRepository<Player, Integer> {

    List<Player> findAllByOrderByLevelDesc();

    List<Player> findAllByOrderByLevelAsc();

    List<Player> findAllByLevel(Integer level);

    @Query(value = "SELECT * FROM Players p WHERE p.immune_until < :currentDateTime AND p.id <> :excludeId AND p.level = :level ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Player> findRandomOpponentByLevelAndWithoutImmunity(@Param("excludeId") int excludeId, @Param("currentDateTime") LocalDateTime currentDateTime, @Param("level") int level);

    Player findByUserId(Integer id);
}
