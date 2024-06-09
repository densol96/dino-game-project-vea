package lv.vea_dino_game.back_end.exceptions;

public class InvalidTokenException extends RuntimeException {
  public InvalidTokenException(String message) {
    super(message);
  }
}
