package backend.academy.hangman.interaction;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.exception.GameInputException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GameInputConsoleHandler implements GameInputHandler {
    private final BufferedReader bufferedReader;

    public GameInputConsoleHandler() {
        InputStream inputStream = DependencyResolver.getInstance()
            .resolve(InputStream.class);
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public String getString() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            throw new GameInputException("Error reading string input", e);
        }
    }

    @Override
    public Integer getInteger() {
        try {
            return Integer.parseInt(getString());
        } catch (NumberFormatException e) {
            throw new GameInputException("Error parsing integer input", e);
        }
    }
}
