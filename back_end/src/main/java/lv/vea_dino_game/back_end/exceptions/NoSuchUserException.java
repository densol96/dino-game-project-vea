package lv.vea_dino_game.back_end.exceptions;

public class NoSuchUserException extends RuntimeException {
  public NoSuchUserException(String message) {
    super(message);
  }
}
