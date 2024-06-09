package lv.vea_dino_game.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.vea_dino_game.back_end.model.User;

import java.util.Optional;


public interface IUserRepo extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);
  
  boolean existsByEmail(String email);

  boolean existsByUsername(String email);

  Optional<User> findByEmailConfirmationToken(String confirmationToken);
}
