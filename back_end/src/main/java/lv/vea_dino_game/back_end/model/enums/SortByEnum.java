package lv.vea_dino_game.back_end.model.enums;

public enum SortByEnum {
  ALL("all"), READ("read"), UNREAD("unread");

  private String value;

  private SortByEnum(String value) {
    this.value = value;
  }
}
