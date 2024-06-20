package lv.vea_dino_game.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.vea_dino_game.back_end.model.UserMailMessage;
import lv.vea_dino_game.back_end.model.dto.HasNewMessagesDto;

public interface IUserMailMessageRepo extends JpaRepository<UserMailMessage, Integer> {
  Boolean existsByUserIdAndIsUnread(Integer id, Boolean isUnread);
}
