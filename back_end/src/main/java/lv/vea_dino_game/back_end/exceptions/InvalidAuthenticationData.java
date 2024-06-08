package lv.vea_dino_game.back_end.exceptions;

public class InvalidAuthenticationData extends RuntimeException {
  public InvalidAuthenticationData(String message) {
    super(message);
  }
}
