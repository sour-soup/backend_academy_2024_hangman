package backend.academy.hangman.game;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.config.GameConfig;
import backend.academy.hangman.exception.InvalidGameParameterException;
import backend.academy.hangman.interaction.GameInputHandler;
import backend.academy.hangman.interaction.GameOutputHandler;
import backend.academy.hangman.model.Word;
import org.aeonbits.owner.ConfigFactory;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import static backend.academy.hangman.game.GameStatus.EXIT;
import static backend.academy.hangman.game.GameStatus.RESTART;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.instancio.Select.field;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HangmanGameSessionTest {
    GameInputHandler mockGameInputHandler;
    GameOutputHandler mockGameOutputHandler;

    @BeforeEach
    void setUp() {
        mockGameInputHandler = Mockito.mock(GameInputHandler.class);
        mockGameOutputHandler = Mockito.mock(GameOutputHandler.class);
        DependencyResolver.getInstance()
            .register(GameInputHandler.class, () -> mockGameInputHandler);
        DependencyResolver.getInstance()
            .register(GameOutputHandler.class, () -> mockGameOutputHandler);
        DependencyResolver.getInstance()
            .register(GameConfig.class, () -> ConfigFactory.create(GameConfig.class));
    }

    @AfterEach
    void tearDown() {
        DependencyResolver.getInstance().clear();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should throw exception when word is empty or null")
    void run_ShouldThrowException_WhenWordIsEmptyOrNull(String wordName) {
        // Arrange
        Word word = Instancio.of(Word.class)
            .set(field(Word::name), wordName)
            .create();
        GameParameters gameParameters = Instancio.of(GameParameters.class)
            .set(field(GameParameters::word), word)
            .create();
        HangmanGameSession gameSession = new HangmanGameSession(gameParameters);

        // Act & Assert
        assertThatThrownBy(gameSession::run)
            .isInstanceOf(InvalidGameParameterException.class)
            .hasMessageContaining("Word");
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, -1, 0})
    @DisplayName("Should throw exception when max attempts is not positive")
    void run_ShouldThrowException_WhenMaxAttemptsIsNotPositive(int maxAttempts) {
        // Arrange
        GameParameters gameParameters = Instancio.of(GameParameters.class)
            .set(field(GameParameters::maxAttempts), maxAttempts)
            .create();
        HangmanGameSession gameSession = new HangmanGameSession(gameParameters);

        // Act & Assert
        assertThatThrownBy(gameSession::run)
            .isInstanceOf(InvalidGameParameterException.class)
            .hasMessageContaining("attempts");
    }

    @Test
    @DisplayName("Should output 'won' when word is guessed")
    void run_ShouldOutputWon_WhenWordIsGuessed() {
        // Arrange
        Word word = Instancio.of(Word.class)
            .set(field(Word::name), "A")
            .create();
        GameParameters gameParameters = new GameParameters(word, 1);
        HangmanGameSession gameSession = new HangmanGameSession(gameParameters);

        when(mockGameInputHandler.getString())
            .thenReturn("A", "no");

        // Act
        gameSession.run();

        verify(mockGameOutputHandler).printMessage(Mockito.contains("You've won"));
    }

    @Test
    @DisplayName("Should output 'lost' when word is not guessed")
    void run_ShouldOutputLost_WhenWordIsNotGuessed() {
        // Arrange
        Word word = Instancio.of(Word.class)
            .set(field(Word::name), "A")
            .create();
        GameParameters gameParameters = new GameParameters(word, 1);
        HangmanGameSession gameSession = new HangmanGameSession(gameParameters);

        when(mockGameInputHandler.getString())
            .thenReturn("B", "no");

        // Act
        gameSession.run();

        verify(mockGameOutputHandler).printMessage(Mockito.contains("You've lost"));
    }

    @Test
    @DisplayName("Should return EXIT when player chooses not to restart")
    void run_ShouldReturnExit_WhenPlayerChoosesNotToRestart() {
        // Arrange
        Word word = Instancio.of(Word.class)
            .set(field(Word::name), "A")
            .create();
        GameParameters gameParameters = new GameParameters(word, 1);
        HangmanGameSession gameSession = new HangmanGameSession(gameParameters);

        when(mockGameInputHandler.getString()).thenReturn("A", "no");

        // Act
        GameStatus result = gameSession.run();

        // Assert
        assertThat(result).isEqualTo(EXIT);
    }

    @Test
    @DisplayName("Should return RESTART when player chooses to restart")
    void run_ShouldReturnRestart_WhenPlayerChoosesToRestart() {
        // Arrange
        Word word = Instancio.of(Word.class)
            .set(field(Word::name), "A")
            .create();
        GameParameters gameParameters = new GameParameters(word, 1);
        HangmanGameSession gameSession = new HangmanGameSession(gameParameters);

        when(mockGameInputHandler.getString()).thenReturn("A", "yes");

        // Act
        GameStatus result = gameSession.run();

        // Assert
        assertThat(result).isEqualTo(RESTART);
    }

}
