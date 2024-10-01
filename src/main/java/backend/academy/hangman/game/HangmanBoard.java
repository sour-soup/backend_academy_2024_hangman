package backend.academy.hangman.game;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.config.GameConfig;
import backend.academy.hangman.exception.ResourceLoadingException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static backend.academy.hangman.utils.RandomUtils.getRandomElements;
import static java.lang.Character.isWhitespace;

public class HangmanBoard implements Board {
    private static final String LOAD_ERROR_MESSAGE = "Failed to load board data: ";
    private final double startPart;
    private final long maxAttempts;

    private String asciiArt;
    private boolean[] activeSymbols;

    public HangmanBoard(long maxAttempts) {
        this("", maxAttempts);
    }

    public HangmanBoard(String asciiArt, long maxAttempts) {
        initializeAsciiArt(asciiArt);
        this.maxAttempts = maxAttempts;
        startPart = DependencyResolver.getInstance().resolve(GameConfig.class).boardStartPart();
    }

    public void loadAsciiArt(String asciiArt) {
        initializeAsciiArt(asciiArt);
    }

    public void loadDefaultAsciiArt() {
        try (InputStream defaultArtStream = getClass().getClassLoader().getResourceAsStream("hangman/board.txt")) {
            loadAsciiArt(Objects.requireNonNull(defaultArtStream));
        } catch (IOException e) {
            throw new ResourceLoadingException(LOAD_ERROR_MESSAGE + e.getMessage(), e);
        }
    }

    public void loadAsciiArt(InputStream asciiArtStream) {
        try {
            initializeAsciiArt(new String(asciiArtStream.readAllBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ResourceLoadingException(LOAD_ERROR_MESSAGE + e.getMessage(), e);
        }
    }

    public String getAsciiArt() {
        return IntStream.range(0, asciiArt.length())
            .mapToObj(i -> (activeSymbols[i] || isWhitespace(asciiArt.charAt(i)))
                ? String.valueOf(asciiArt.charAt(i)) : " ")
            .collect(Collectors.joining());
    }

    public void addAttempt() {
        long totalSymbols = countNonSpaceSymbols();
        long activeSymbolsCount = IntStream.range(0, activeSymbols.length)
            .filter(i -> activeSymbols[i]).count();
        long symbolsToActivate = (long) Math.min(totalSymbols - activeSymbolsCount,
            Math.ceil(totalSymbols * (1 - startPart) / maxAttempts));
        activateSymbols(symbolsToActivate);
    }

    private void initializeAsciiArt(String asciiArt) {
        this.asciiArt = asciiArt;
        this.activeSymbols = new boolean[asciiArt.length()];
        activateSymbols((long) (countNonSpaceSymbols() * startPart));
    }

    private long countNonSpaceSymbols() {
        return asciiArt.chars()
            .filter(ch -> !isWhitespace(ch))
            .count();
    }

    private void activateSymbols(long n) {
        List<Integer> inactiveSymbols = IntStream.range(0, asciiArt.length())
            .filter(i -> !isWhitespace(asciiArt.charAt(i)))
            .filter(i -> !activeSymbols[i])
            .boxed().toList();

        getRandomElements(inactiveSymbols, n)
            .forEach((i) -> activeSymbols[i] = true);
    }
}
