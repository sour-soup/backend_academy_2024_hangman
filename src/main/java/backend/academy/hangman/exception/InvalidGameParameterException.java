package backend.academy.hangman.exception;

public class InvalidGameParameterException extends RuntimeException {
    public InvalidGameParameterException(String message) {
        super(message);
    }

    public InvalidGameParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
