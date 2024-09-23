package backend.academy.hangman.game;

import backend.academy.hangman.exception.ResourceLoadingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void loadAsciiArt_ShouldLoadDataCorrectly_WhenStringIsGiven() {
        // Arrange
        String art = "ART";

        // Act
        board.loadAsciiArt(art);

        // Assert
        assertThat(board.getAsciiArt().length()).isEqualTo(art.length());
    }

    @Test
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
    void loadAsciiArt_ShouldThrowException_WhenInputStreamIsNull() {
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
    void updateStatus_ShouldActivateAllSymbols_AfterMaxAttempts() {
        String art = "ART";
        board.loadAsciiArt(art);

        // Act
        board.updateStatus(5, 5);

        // Assert
        assertThat(board.getAsciiArt()).isEqualTo(art);
    }
}
