package backend.academy.hangman.exception;

public class GameInputException extends RuntimeException {
    public GameInputException(String message) {
        super(message);
    }

    public GameInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
