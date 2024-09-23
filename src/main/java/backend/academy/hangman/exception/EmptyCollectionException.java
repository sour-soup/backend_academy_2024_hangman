package backend.academy.hangman.exception;

public class EmptyCollectionException extends RuntimeException {
    public EmptyCollectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyCollectionException(String message) {
        super(message);
    }
}
