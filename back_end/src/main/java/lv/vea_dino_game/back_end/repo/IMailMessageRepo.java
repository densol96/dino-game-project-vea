package lv.vea_dino_game.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.vea_dino_game.back_end.model.MailMessage;

public interface IMailMessageRepo extends JpaRepository<MailMessage, Integer> {
  
}
