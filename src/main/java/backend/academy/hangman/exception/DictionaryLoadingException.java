package backend.academy.hangman.exception;

public class DictionaryLoadingException extends RuntimeException {
    public DictionaryLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DictionaryLoadingException(String message) {
        super(message);
    }
}
