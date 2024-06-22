package lv.vea_dino_game.back_end.model.enums;

public enum MailFilterByEnum {
  ALL("all"),
  READ("read"),
  UNREAD("unread");

  private String value;

  private MailFilterByEnum(String value) {
    this.value = value;
  }
}
