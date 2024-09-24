package backend.academy.hangman.exception;

public class GameOutputException extends RuntimeException {
    public GameOutputException(String message) {
        super(message);
    }

    public GameOutputException(String message, Throwable cause) {
        super(message, cause);
    }
}
