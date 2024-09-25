package backend.academy.hangman.game;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.interaction.GameInputHandler;
import backend.academy.hangman.interaction.GameOutputHandler;
import backend.academy.hangman.model.Word;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GameSession {
    private static final String WIN_ASCII_ART = """

        ██╗   ██╗ ██████╗ ██╗   ██╗    ██╗    ██╗██╗███╗   ██╗
        ╚██╗ ██╔╝██╔═══██╗██║   ██║    ██║    ██║██║████╗  ██║
         ╚████╔╝ ██║   ██║██║   ██║    ██║ █╗ ██║██║██╔██╗ ██║
          ╚██╔╝  ██║   ██║██║   ██║    ██║███╗██║██║██║╚██╗██║
           ██║   ╚██████╔╝╚██████╔╝    ╚███╔███╔╝██║██║ ╚████║
           ╚═╝    ╚═════╝  ╚═════╝      ╚══╝╚══╝ ╚═╝╚═╝  ╚═══╝
                                                             \s

        """;
    private static final String ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static final String HINT_COMMAND = "HINT";
    private static final String YES_RESPONSE = "yes";
    private static final String INVALID_INPUT_MESSAGE =
        "Invalid input. Please enter a single valid letter from the Russian alphabet.";

    private long currentAttempts;
    private final long maxAttempts;
    private final Word word;
    private final Set<Character> usedLetters;
    private final Board board;
    private final GameInputHandler gameInputHandler;
    private final GameOutputHandler gameOutputHandler;
    private boolean hintUsed;

    public GameSession(Word word, long maxAttempts) {
        this.word = word;
        this.maxAttempts = maxAttempts;
        currentAttempts = 0;
        usedLetters = new HashSet<>();
        board = new Board(maxAttempts);
        board.loadDefaultAsciiArt();
        gameInputHandler = DependencyResolver.getInstance().resolve(GameInputHandler.class);
        gameOutputHandler = DependencyResolver.getInstance().resolve(GameOutputHandler.class);
    }

    public GameStatus run() {
        if (isGameActive()) {
            do {
                displayGameState();
                Optional<Character> guessedLetter = getValidInput();
                guessedLetter.ifPresent(this::processGuess);
            } while (isGameActive());
        }
        displayGameResult();
        return (askToPlayAgain() ? GameStatus.RESTART : GameStatus.EXIT);
    }

    private void displayGameState() {
        gameOutputHandler.clear();
        gameOutputHandler.printMessage(board.getAsciiArt());
        gameOutputHandler.printMessage("Current word: %s".formatted(getMaskedWord()));
        gameOutputHandler.printMessage("Used letters: %s".formatted(usedLetters));
        gameOutputHandler.printMessage("Remaining attempts: %d".formatted(maxAttempts - currentAttempts));
        gameOutputHandler.printMessage("Hint: %s".formatted(hintUsed ? word.hint() : " available"));
        gameOutputHandler.printMessage("Enter a letter (or type %s for a hint):".formatted(HINT_COMMAND));
    }

    private Optional<Character> getValidInput() {
        while (true) {
            String input = gameInputHandler.getString().toUpperCase();
            if (input.length() == 1 && ALPHABET.contains(input)) {
                char guessedLetter = input.charAt(0);
                if (!usedLetters.contains(guessedLetter)) {
                    return Optional.of(guessedLetter);
                } else {
                    gameOutputHandler.printMessage("You have already used this letter.");
                }
            } else if (!hintUsed && HINT_COMMAND.equalsIgnoreCase(input)) {
                hintUsed = true;
                return Optional.empty();
            } else {
                gameOutputHandler.printMessage(INVALID_INPUT_MESSAGE);
            }
        }
    }

    private void processGuess(char guessedLetter) {
        usedLetters.add(guessedLetter);
        if (!word.name().toUpperCase().contains(String.valueOf(guessedLetter))) {
            currentAttempts++;
            board.addAttempt();
        }
    }

    private void displayGameResult() {
        gameOutputHandler.clear();
        if (isWordGuessed()) {
            gameOutputHandler.printMessage(WIN_ASCII_ART);
            gameOutputHandler.printMessage("Congratulations! You've won! The word was: %s".formatted(word.name()));
        } else {
            gameOutputHandler.printMessage(board.getAsciiArt());
            gameOutputHandler.printMessage("You've lost. The correct word was: %s".formatted(word.name()));
        }
    }

    public boolean askToPlayAgain() {
        gameOutputHandler.printMessage("Do you want to play again? (yes/no)");
        String input = gameInputHandler.getString();
        return YES_RESPONSE.equalsIgnoreCase(input);
    }

    private boolean isGameActive() {
        return currentAttempts < maxAttempts && !isWordGuessed();
    }

    private String getMaskedWord() {
        StringBuilder maskedWord = new StringBuilder();
        for (char letter : word.name().toUpperCase().toCharArray()) {
            maskedWord.append(usedLetters.contains(letter) ? letter : '_');
            maskedWord.append(' ');
        }
        return maskedWord.toString();
    }

    private boolean isWordGuessed() {
        return word.name().toUpperCase().chars().allMatch(c -> usedLetters.contains((char) c));
    }
}