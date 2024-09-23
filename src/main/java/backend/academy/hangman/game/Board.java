package backend.academy.hangman.game;

import backend.academy.hangman.exception.ResourceLoadingException;
import backend.academy.hangman.utils.RandomUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {
    private static final String LOAD_ERROR_MESSAGE = "Failed to load board data: ";
    private static final double START_PART = 0.1;

    private String asciiArt;
    private boolean[] activeSymbols;

    public Board() {
        this("");
    }

    public Board(String asciiArt) {
        initializeAsciiArt(asciiArt);
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
            .mapToObj(i -> (activeSymbols[i] || Character.isWhitespace(asciiArt.charAt(i)))
                ? String.valueOf(asciiArt.charAt(i)) : " ")
            .collect(Collectors.joining());
    }

    public void updateStatus(long currentAttempts, long maxAttempts) {
        long totalSymbols = countNonSpaceSymbols();
        long activeSymbolsCount = IntStream.range(0, activeSymbols.length).filter(i -> activeSymbols[i]).count();

        double percentage = START_PART + (double) currentAttempts / maxAttempts * (1 - START_PART);
        long expectedActiveSymbols = currentAttempts == maxAttempts ? totalSymbols : (long) (totalSymbols * percentage);
        long symbolsToActivate = expectedActiveSymbols - activeSymbolsCount;
        activateSymbols(symbolsToActivate);
    }

    public void clear() {
        activeSymbols = new boolean[asciiArt.length()];
        activateSymbols((long) (countNonSpaceSymbols() * START_PART));
    }

    private void initializeAsciiArt(String asciiArt) {
        this.asciiArt = asciiArt;
        this.activeSymbols = new boolean[asciiArt.length()];
        activateSymbols((long) (countNonSpaceSymbols() * START_PART));
    }

    private long countNonSpaceSymbols() {
        return asciiArt.chars()
            .filter(ch -> !Character.isWhitespace(ch))
            .count();
    }

    private void activateSymbols(long n) {
        List<Integer> inactiveSymbols = IntStream.range(0, asciiArt.length())
            .filter(i -> !Character.isWhitespace(asciiArt.charAt(i)))
            .filter(i -> !activeSymbols[i])
            .boxed().toList();

        RandomUtils.getRandomElements(inactiveSymbols, n)
            .forEach((i) -> activeSymbols[i] = true);
    }
}
