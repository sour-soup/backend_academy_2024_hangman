package backend.academy.hangman.exception;

public class ServiceNotRegisteredException extends RuntimeException {
    public ServiceNotRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceNotRegisteredException(String message) {
        super(message);
    }
}
