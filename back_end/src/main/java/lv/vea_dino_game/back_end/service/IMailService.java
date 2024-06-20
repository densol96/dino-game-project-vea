package lv.vea_dino_game.back_end.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import lv.vea_dino_game.back_end.model.dto.BasicMailDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.HasNewMessagesDto;
import lv.vea_dino_game.back_end.model.dto.MailDto;

public interface IMailService {
  BasicMessageResponse sendMail(MailDto dto);

  HasNewMessagesDto hasUnreadMessages();

  List<BasicMailDto> getAllIncomingMail(Integer page);

  List<BasicMailDto> getAllOutgoingMail(Integer page);

  Integer getNumberOfPagesForAllIncomingMail();

  Integer getNumberOfPagesForAllOutgoingMail();
}
