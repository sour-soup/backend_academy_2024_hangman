package backend.academy.hangman.game;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.config.GameConfig;
import backend.academy.hangman.interaction.GameInputHandler;
import backend.academy.hangman.interaction.GameOutputHandler;
import backend.academy.hangman.model.Word;
import backend.academy.hangman.service.DictionaryService;
import java.util.Optional;
import org.aeonbits.owner.ConfigFactory;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GameMenuTest {
    GameInputHandler mockGameInputHandler;
    GameOutputHandler mockGameOutputHandler;
    DictionaryService mockDictionaryService;

    @BeforeEach
    void setUp() {
        mockGameInputHandler = Mockito.mock(GameInputHandler.class);
        mockGameOutputHandler = Mockito.mock(GameOutputHandler.class);
        mockDictionaryService = Mockito.mock(DictionaryService.class);

        DependencyResolver.getInstance()
            .register(GameInputHandler.class, () -> mockGameInputHandler);
        DependencyResolver.getInstance()
            .register(GameOutputHandler.class, () -> mockGameOutputHandler);
        DependencyResolver.getInstance()
            .register(DictionaryService.class, () -> mockDictionaryService);
        DependencyResolver.getInstance()
            .register(GameConfig.class, () -> ConfigFactory.create(GameConfig.class));

        Mockito.when(mockDictionaryService.getRandomWord())
            .thenReturn(Instancio.create(Word.class));
    }

    @AfterEach
    void tearDown() {
        DependencyResolver.getInstance().clear();
    }

    @Test
    void run_ShouldReturnEmptyOptional_WhenPlayerChoosesExit() {
        // Arrange
        GameMenu gameMenu = new GameMenu();

        Mockito.when(mockGameInputHandler.getString()).thenReturn("0");

        // Act
        Optional<GameParameters> gameParameters = gameMenu.run();

        // Assert
        assertFalse(gameParameters.isPresent());
    }

    @Test
    void run_ShouldReturnDefaultMaxAttempts_WhenPlayerChoosesDefaultGame() {
        // Arrange
        GameMenu gameMenu = new GameMenu();
        long expectedMaxAttempts = DependencyResolver.getInstance()
            .resolve(GameConfig.class).maxAttempts();

        Mockito.when(mockGameInputHandler.getInteger())
            .thenReturn(1, 0, 0);

        // Act
        Optional<GameParameters> gameParameters = gameMenu.run();

        // Assert
        assertThat(gameParameters.orElseThrow().maxAttempts())
            .isEqualTo(expectedMaxAttempts);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 100})
    void run_ShouldReturnChosenMaxAttempts(long expectedMaxAttempts) {
        // Arrange
        GameMenu gameMenu = new GameMenu();

        Mockito.when(mockGameInputHandler.getInteger())
            .thenReturn(1, 0, (int) expectedMaxAttempts);

        // Act
        Optional<GameParameters> gameParameters = gameMenu.run();

        // Assert
        assertThat(gameParameters.orElseThrow().maxAttempts())
            .isEqualTo(expectedMaxAttempts);
    }
}
