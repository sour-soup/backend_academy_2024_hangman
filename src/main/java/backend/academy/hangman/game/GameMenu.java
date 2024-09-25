package backend.academy.hangman.game;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.config.GameConfig;
import backend.academy.hangman.interaction.GameInputHandler;
import backend.academy.hangman.interaction.GameOutputHandler;
import backend.academy.hangman.model.Category;
import backend.academy.hangman.model.Word;
import backend.academy.hangman.service.DictionaryService;
import java.util.List;
import java.util.Optional;

public class GameMenu {
    private final GameInputHandler gameInputHandler;
    private final GameOutputHandler gameOutputHandler;
    private final DictionaryService dictionaryService;
    private final GameConfig gameConfig;

    public GameMenu() {
        this.gameInputHandler = DependencyResolver.getInstance().resolve(GameInputHandler.class);
        this.gameOutputHandler = DependencyResolver.getInstance().resolve(GameOutputHandler.class);
        this.dictionaryService = DependencyResolver.getInstance().resolve(DictionaryService.class);
        this.gameConfig = DependencyResolver.getInstance().resolve(GameConfig.class);
    }

    public Optional<GameParameters> run() {
        gameOutputHandler.clear();

        if (selectMainMenuOption() == 0) {
            return Optional.empty();
        }

        Optional<Category> selectedCategory = selectCategory();

        Word word = (selectedCategory.map(dictionaryService::getRandomWordByCategory)
            .orElseGet(dictionaryService::getRandomWord));

        int attempts = selectAttempts();

        return Optional.of(new GameParameters(word, attempts));
    }

    private int selectMainMenuOption() {
        gameOutputHandler.printMessage("1) Start game");
        gameOutputHandler.printMessage("0) Exit game");
        return getNumberInRange(0, 1);
    }

    private Optional<Category> selectCategory() {
        List<Category> categories = dictionaryService.getAllCategories();
        gameOutputHandler.printMessage("Select a category:");
        gameOutputHandler.printMessage("0) Random category");

        for (int i = 0; i < categories.size(); i++) {
            gameOutputHandler.printMessage("%d) %s".formatted(i + 1, categories.get(i).name()));
        }

        int categoryIndex = getNumberInRange(0, categories.size());

        if (categoryIndex == 0) {
            return Optional.empty();
        }
        --categoryIndex;

        return Optional.of(categories.get(categoryIndex));
    }

    private int selectAttempts() {
        gameOutputHandler.printMessage("Select difficulty (number of attempts):");
        gameOutputHandler.printMessage("0) Default (%d)".formatted(gameConfig.maxAttempts()));

        int attempts = getNumberInRange(0, Integer.MAX_VALUE);

        return (attempts == 0 ? gameConfig.maxAttempts() : attempts);
    }

    int getNumberInRange(int minValue, int maxValue) {
        while (true) {
            Integer input = gameInputHandler.getInteger();
            if (input != null && input >= minValue && input <= maxValue) {
                return input;
            }
            gameOutputHandler.printMessage(
                "Please enter a valid number between %d and %d".formatted(minValue, maxValue));
        }
    }
}
