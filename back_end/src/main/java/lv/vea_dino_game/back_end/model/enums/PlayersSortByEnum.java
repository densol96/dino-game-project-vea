package lv.vea_dino_game.back_end.model.enums;

public enum PlayersSortByEnum {
  EXPERIENCE("experience"),
  STOLEN("combatStats.currencyWon"),
  WINRATE("placeholder");

  private String fieldName;

  private PlayersSortByEnum(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public String toString() {
    return this.fieldName;
  }
}
