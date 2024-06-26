package lv.vea_dino_game.back_end.service;

import java.util.List;



import lv.vea_dino_game.back_end.model.dto.BasicMailDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.HasNewMessagesDto;
import lv.vea_dino_game.back_end.model.dto.MailDto;
import lv.vea_dino_game.back_end.model.enums.MailFilterByEnum;

public interface IMailService {
  BasicMessageResponse sendMail(MailDto dto);

  HasNewMessagesDto hasUnreadMessages();

  List<BasicMailDto> getAllIncomingMail(Integer page, String filterBy);

  List<BasicMailDto> getAllOutgoingMail(Integer page);

  Integer getNumberOfPagesForAllIncomingMail(String filterBy);

  Integer getNumberOfPagesForAllOutgoingMail();

  BasicMailDto getMailById(Integer id);

  BasicMessageResponse removeMail(Integer id);

  void sendNotificationFromAdmin(String usernameTo, String title, String text);
}
