package backend.academy.hangman.game;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.config.GameConfig;
import backend.academy.hangman.interaction.GameInputHandler;
import backend.academy.hangman.interaction.GameOutputHandler;
import backend.academy.hangman.model.Word;
import java.util.Optional;
import org.aeonbits.owner.ConfigFactory;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

class HangmanGameMenuTest {
    GameInputHandler mockGameInputHandler;
    GameOutputHandler mockGameOutputHandler;
    Dictionary mockDictionary;

    @BeforeEach
    void setUp() {
        mockGameInputHandler = Mockito.mock(GameInputHandler.class);
        mockGameOutputHandler = Mockito.mock(GameOutputHandler.class);
        mockDictionary = Mockito.mock(Dictionary.class);

        DependencyResolver.getInstance()
            .register(GameInputHandler.class, () -> mockGameInputHandler);
        DependencyResolver.getInstance()
            .register(GameOutputHandler.class, () -> mockGameOutputHandler);
        DependencyResolver.getInstance()
            .register(Dictionary.class, () -> mockDictionary);
        DependencyResolver.getInstance()
            .register(GameConfig.class, () -> ConfigFactory.create(GameConfig.class));

        Mockito.when(mockDictionary.getRandomWord())
            .thenReturn(Instancio.create(Word.class));
    }

    @AfterEach
    void tearDown() {
        DependencyResolver.getInstance().clear();
    }

    @Test
    @DisplayName("Should return empty optional when player chooses to exit")
    void run_ShouldReturnEmptyOptional_WhenPlayerChoosesExit() {
        // Arrange
        HangmanGameMenu gameMenu = new HangmanGameMenu();

        Mockito.when(mockGameInputHandler.getString()).thenReturn("0");

        // Act
        Optional<GameParameters> gameParameters = gameMenu.run();

        // Assert
        assertFalse(gameParameters.isPresent());
    }

    @Test
    @DisplayName("Should return default max attempts when player chooses default game")
    void run_ShouldReturnDefaultMaxAttempts_WhenPlayerChoosesDefaultGame() {
        // Arrange
        HangmanGameMenu gameMenu = new HangmanGameMenu();
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
    @DisplayName("Should return chosen max attempts based on user input")
    void run_ShouldReturnChosenMaxAttempts(long expectedMaxAttempts) {
        // Arrange
        HangmanGameMenu gameMenu = new HangmanGameMenu();

        Mockito.when(mockGameInputHandler.getInteger())
            .thenReturn(1, 0, (int) expectedMaxAttempts);

        // Act
        Optional<GameParameters> gameParameters = gameMenu.run();

        // Assert
        assertThat(gameParameters.orElseThrow().maxAttempts())
            .isEqualTo(expectedMaxAttempts);
    }
}
