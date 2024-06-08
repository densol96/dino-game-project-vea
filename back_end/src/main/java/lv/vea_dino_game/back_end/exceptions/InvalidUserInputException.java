package lv.vea_dino_game.back_end.exceptions;

public class InvalidUserInputException extends RuntimeException {
  public InvalidUserInputException(String message) {
    super(message);
  }
}
