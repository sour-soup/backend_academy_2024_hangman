package backend.academy.hangman.utils;

import backend.academy.hangman.exception.EmptyCollectionException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RandomUtilsTest {

    @Test
    void getRandomElement_ShouldReturnElement_WhenCollectionHasElements() {
        // Arrange
        List<String> elements = List.of("apple", "banana", "cherry");

        // Act
        String randomElement = RandomUtils.getRandomElement(elements);

        // Assert
        assertThat(elements).contains(randomElement);
    }

    @Test
    public void getRandomElement_ShouldThrowEmptyCollectionException_WhenCollectionIsEmpty() {
        // Arrange
        List<String> emptyList = List.of();

        // Assert & Act
        assertThatThrownBy(() -> RandomUtils.getRandomElement(emptyList))
            .isInstanceOf(EmptyCollectionException.class);
    }
}
