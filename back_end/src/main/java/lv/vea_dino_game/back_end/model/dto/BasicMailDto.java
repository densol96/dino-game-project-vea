package lv.vea_dino_game.back_end.model.dto;

import java.time.LocalDateTime;

import lv.vea_dino_game.back_end.model.enums.MailType;

public record BasicMailDto(
  Integer id,
  String from,
  String to,
  String title,
  String text,
  LocalDateTime sentAt,
  Boolean isUnread,
  MailType type
) {
}
