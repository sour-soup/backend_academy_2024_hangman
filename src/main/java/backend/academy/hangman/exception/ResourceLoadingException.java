package backend.academy.hangman.exception;

public class ResourceLoadingException extends RuntimeException {
    public ResourceLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceLoadingException(String message) {
        super(message);
    }
}
