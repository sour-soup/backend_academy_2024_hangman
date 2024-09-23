package backend.academy.hangman.exception;

public class InvalidCollectionRequestException extends RuntimeException {
    public InvalidCollectionRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCollectionRequestException(String message) {
        super(message);
    }
}
