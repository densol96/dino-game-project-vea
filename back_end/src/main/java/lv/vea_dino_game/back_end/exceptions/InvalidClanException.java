package lv.vea_dino_game.back_end.exceptions;

public class InvalidClanException extends RuntimeException{
    public InvalidClanException(String message) {
        super(message);
    }
}