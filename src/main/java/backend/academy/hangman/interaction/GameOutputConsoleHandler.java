package backend.academy.hangman.interaction;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.exception.GameOutputException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class GameOutputConsoleHandler implements GameOutputHandler {
    private final BufferedWriter bufferedWriter;

    public GameOutputConsoleHandler() {
        OutputStream outputStream = DependencyResolver.getInstance()
            .resolve(OutputStream.class);
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,  StandardCharsets.UTF_8));
    }

    @Override
    public void printMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new GameOutputException("Error writing output message", e);
        }
    }

    @Override
    public void clear() {
        printMessage("\n\n\n\n\n\n\n");
    }
}
