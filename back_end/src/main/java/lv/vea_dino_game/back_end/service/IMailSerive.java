package lv.vea_dino_game.back_end.service;

import org.springframework.http.ResponseEntity;

import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.MailDto;

public interface IMailSerive {
  BasicMessageResponse sendMail(MailDto dto);
}
