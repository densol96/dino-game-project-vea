package lv.vea_dino_game.back_end.exceptions;

public class ServiceCurrentlyUnavailableException extends RuntimeException {
  public ServiceCurrentlyUnavailableException(String message) {
    super(message);
  }
}
