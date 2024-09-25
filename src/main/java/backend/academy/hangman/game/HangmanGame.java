package backend.academy.hangman.game;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.interaction.GameOutputHandler;
import java.util.Optional;

public class HangmanGame {
    private static final String EXITING_THE_GAME = "Exiting the game. Goodbye!";

    private final GameMenu gameMenu;
    private final GameOutputHandler gameOutputHandler;

    public HangmanGame() {
        gameMenu = new GameMenu();
        gameOutputHandler = DependencyResolver.getInstance().resolve(GameOutputHandler.class);
    }

    public void run() {
        while (true) {
            Optional<GameParameters> gameParametersOptional = gameMenu.run();
            if (gameParametersOptional.isEmpty()) {
                gameOutputHandler.printMessage(EXITING_THE_GAME);
                break;
            }
            GameParameters gameParameters = gameParametersOptional.get();
            GameSession gameSession = new GameSession(gameParameters.word(), gameParameters.maxAttempts());

            GameStatus status = gameSession.run();
            if (status == GameStatus.EXIT) {
                gameOutputHandler.printMessage(EXITING_THE_GAME);
                break;
            }
        }
    }
}
