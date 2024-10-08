package backend.academy.hangman.game;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.config.GameConfig;
import backend.academy.hangman.exception.ResourceLoadingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HangmanBoardTest {
    private HangmanBoard board;

    @BeforeEach
    void setUp() {
        DependencyResolver.getInstance().clear();

        DependencyResolver.getInstance()
            .register(GameConfig.class, () -> ConfigFactory.create(GameConfig.class));

        board = new HangmanBoard(1);
    }

    @AfterEach
    void tearDown() {
        DependencyResolver.getInstance().clear();
    }

    @Test
    @DisplayName("Should load ASCII art data correctly when a string is given")
    void loadAsciiArt_ShouldLoadDataCorrectly_WhenStringIsGiven() {
        // Arrange
        String art = "ART";

        // Act
        board.loadAsciiArt(art);

        // Assert
        assertThat(board.getAsciiArt().length()).isEqualTo(art.length());
    }

    @Test
    @DisplayName("Should load ASCII art data when input stream is provided")
    void loadAsciiArt_ShouldLoadData_WhenInputStreamIsCorrectly() {
        // Arrange
        String art = "ART";
        InputStream inputStream = new ByteArrayInputStream(art.getBytes());

        // Act
        board.loadAsciiArt(inputStream);

        // Assert
        assertThat(board.getAsciiArt().length()).isEqualTo(art.length());
    }

    @Test
    @DisplayName("Should throw exception when input stream is throw exception")
    void loadAsciiArt_ShouldThrowException_WhenInputStreamIsThrowException() {
        // Arrange
        try (InputStream inputStreamMock = mock(InputStream.class)) {
            when(inputStreamMock.readAllBytes()).thenThrow(new IOException(""));

            // Act & Assert
            assertThatThrownBy(() -> board.loadAsciiArt(inputStreamMock))
                .isInstanceOf(ResourceLoadingException.class)
                .hasMessageContaining("Failed to load board data: ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should activate all symbols after max attempts")
    void addAttempt_ShouldActivateAllSymbols_AfterMaxAttempts() {
        String art = "ART";
        board.loadAsciiArt(art);

        // Act
        board.addAttempt();

        // Assert
        assertThat(board.getAsciiArt()).isEqualTo(art);
    }
}
