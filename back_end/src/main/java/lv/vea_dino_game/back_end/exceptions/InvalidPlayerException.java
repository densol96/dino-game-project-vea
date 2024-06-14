package lv.vea_dino_game.back_end.exceptions;

public class InvalidPlayerException extends RuntimeException{
    public InvalidPlayerException(String message) {
        super(message);
    }
}
