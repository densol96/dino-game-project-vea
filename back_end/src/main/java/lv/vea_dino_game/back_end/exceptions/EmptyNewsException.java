package lv.vea_dino_game.back_end.exceptions;

public class EmptyNewsException extends RuntimeException {
    public EmptyNewsException(String message) {
        super(message);
    }
}
