package lv.vea_dino_game.back_end.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import lv.vea_dino_game.back_end.model.UserMailMessage;
import lv.vea_dino_game.back_end.model.dto.HasNewMessagesDto;
import lv.vea_dino_game.back_end.model.enums.MailType;

public interface IUserMailMessageRepo extends JpaRepository<UserMailMessage, Integer> {
  Boolean existsByUserIdAndIsUnread(Integer id, Boolean isUnread);

  List<UserMailMessage> findAllByUserUsernameAndType(String username, MailType type, Pageable pageable);

  List<UserMailMessage> findAllByUserUsernameAndTypeAndIsUnread(String username, MailType type, Pageable pageable, Boolean isUnreadValue);

  Integer countByUserUsernameAndType(String username, MailType type);

  Integer countByUserUsernameAndTypeAndIsUnread(String username, MailType type, Boolean value);
}
