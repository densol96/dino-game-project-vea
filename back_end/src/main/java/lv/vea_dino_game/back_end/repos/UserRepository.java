package lv.vea_dino_game.back_end.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.vea_dino_game.back_end.model.User;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);
  
  boolean existsByEmail(String email);

  boolean existsByUsername(String email);
}
