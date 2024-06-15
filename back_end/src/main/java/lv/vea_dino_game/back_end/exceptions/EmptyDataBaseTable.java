package lv.vea_dino_game.back_end.exceptions;

public class EmptyDataBaseTable extends RuntimeException {
  public EmptyDataBaseTable(String message) {
    super(message);
  }
}
