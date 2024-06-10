package lv.vea_dino_game.back_end.exceptions;

public class EmptyClanException extends RuntimeException{
    public EmptyClanException(String message) {
        super(message);
    }
}
