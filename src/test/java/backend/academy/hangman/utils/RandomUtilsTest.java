package backend.academy.hangman.utils;

import backend.academy.hangman.exception.InvalidCollectionRequestException;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
            .isInstanceOf(InvalidCollectionRequestException.class);
    }

    @Test
    public void getRandomElements_ReturnsCorrectList(){
        // Arrange
        List<Integer> collection = List.of(1,2,3,4,5);
        int n = 3;

        // Act
        List<Integer> randomElements = RandomUtils.getRandomElements(collection, n);

        // Assert
        assertThat(collection).containsAll(randomElements);
        assertThat(randomElements).hasSize(n);
    }

    @Test
    public void getRandomElements_ReturnsAllElements_WhenNEqualsCollectionSize(){
        // Arrange
        List<Integer> collection = List.of(1,2,3,4,5);
        int n = collection.size();

        // Act
        List<Integer> randomElements = RandomUtils.getRandomElements(collection, n);

        // Assert
        assertThat(collection).containsExactlyInAnyOrderElementsOf(randomElements);
    }

    @Test
    public void getRandomElements_ReturnsEmptyList_WhenNIsZero(){
        // Arrange
        List<Integer> collection = List.of(1,2,3,4,5);
        int n = 0;

        // Act
        List<Integer> randomElements = RandomUtils.getRandomElements(collection, n);

        // Assert
        assertThat(randomElements).isEmpty();
    }

    @Test
    public void getRandomElements_ReturnsEmptyList_WhenCollectionIsEmptyAndNIsZero(){
        // Arrange
        List<Integer> collection = List.of();
        int n = 0;

        // Act
        List<Integer> randomElements = RandomUtils.getRandomElements(collection, n);

        // Assert
        assertThat(randomElements).isEmpty();
    }

    @Test
    public  void getRandomElements_ThrowsException_WhenNIsNegative(){
        List<Integer> collection = List.of(1,2,3,4,5);
        int n = -1;

        // Act & Assert
        assertThatThrownBy(() -> RandomUtils.getRandomElements(collection, n))
            .isInstanceOf(InvalidCollectionRequestException.class)
            .hasMessage("Invalid number of elements requested");
    }

    @Test
    public  void getRandomElements_ThrowsException_WhenNExceedsCollectionSize(){
        List<Integer> collection = List.of(1,2,3,4,5);
        int n = collection.size() + 1;

        // Act & Assert
        assertThatThrownBy(() -> RandomUtils.getRandomElements(collection, n))
            .isInstanceOf(InvalidCollectionRequestException.class)
            .hasMessage("Invalid number of elements requested");
    }


}
