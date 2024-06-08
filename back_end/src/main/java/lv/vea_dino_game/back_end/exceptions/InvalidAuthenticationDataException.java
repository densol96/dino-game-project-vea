package lv.vea_dino_game.back_end.exceptions;

public class InvalidAuthenticationDataException extends RuntimeException {
  public InvalidAuthenticationDataException(String message) {
    super(message);
  }
}
