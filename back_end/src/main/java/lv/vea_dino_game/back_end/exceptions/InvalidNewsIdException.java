package lv.vea_dino_game.back_end.exceptions;

public class InvalidNewsIdException extends RuntimeException {
    public InvalidNewsIdException(String message) {
        super(message);
    }
}
