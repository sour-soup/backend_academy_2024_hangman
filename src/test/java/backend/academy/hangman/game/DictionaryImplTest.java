package backend.academy.hangman.game;

import backend.academy.hangman.exception.ResourceLoadingException;
import backend.academy.hangman.model.Category;
import backend.academy.hangman.model.Word;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DictionaryImplTest {
    private DictionaryImpl dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new DictionaryImpl();
        dictionary.loadDictionaryData(getExampleInputStream());
    }

    @AfterEach
    void tearDown() {
        dictionary = null;
    }

    @Test
    @DisplayName("Should load dictionary data correctly")
    void loadDictionaryData_ShouldLoadDictionaryDataCorrectly() {
        // Act
        dictionary.loadDictionaryData(getExampleInputStream());

        // Assert
        List<Category> actualCategories = dictionary.getAllCategories();
        Word actualWord = dictionary.getRandomWord();

        assertThat(actualCategories).isEqualTo(getExampleCategories());
        assertThat(getExampleWords()).contains(actualWord);
    }

    @Test
    @DisplayName("Should throw exception when JSON is invalid")
    void loadDictionaryData_ShouldThrowException_WhenJsonIsInvalid() {
        // Arrange
        String invalidJsonData = "baobab";
        InputStream inputStream = new ByteArrayInputStream(invalidJsonData.getBytes());

        // Act & Assert
        assertThatThrownBy(() -> dictionary.loadDictionaryData(inputStream))
            .isInstanceOf(ResourceLoadingException.class);
    }

    @Test
    @DisplayName("Should throw exception when input stream is null")
    void loadDictionaryData_ShouldThrowException_WhenInputStreamIsNull() {
        // Arrange (Given)
        InputStream inputStream = InputStream.nullInputStream();

        // Act & Assert (When & Then)
        assertThatThrownBy(() -> dictionary.loadDictionaryData(inputStream))
            .isInstanceOf(ResourceLoadingException.class);
    }

    @Test
    @DisplayName("Should return all categories")
    void getAllCategories() {
        // Act
        List<Category> actualCategories = dictionary.getAllCategories();

        // Assert
        assertThat(actualCategories).containsExactlyInAnyOrderElementsOf(getExampleCategories());
    }

    @Test
    @DisplayName("Should return a random word")
    void getRandomWord() {
        // Act
        Word actualWord = dictionary.getRandomWord();

        // Assert
        assertThat(getExampleWords()).contains(actualWord);
    }

    @Test
    @DisplayName("Should return a random word by category")
    void getRandomWordByCategory() {
        // Act
        Category category = dictionary.getAllCategories().getFirst();
        Word actualWord = dictionary.getRandomWordByCategory(category);

        List<Word> expectedWords = getExampleWords().stream()
            .filter(word -> Objects.equals(word.category().id(), category.id()))
            .toList();

        // Assert
        assertThat(expectedWords).containsExactly(actualWord);
    }

    private static InputStream getExampleInputStream() {
        String jsonData = """
            {
              "categories": [
                {
                  "id": 1,
                  "name": "Фрукты",
                  "description": "Различные виды фруктов"
                },
                {
                  "id": 2,
                  "name": "Овощи",
                  "description": "Разнообразные овощи"
                }
              ],
              "words": [
                {
                  "id": 1,
                  "name": "Яблоко",
                  "categoryId": 1,
                  "hint": "Красный или зелёный фрукт"
                },
                {
                  "id": 2,
                  "name": "Морковь",
                  "categoryId": 2,
                  "hint": "Оранжевый овощ"
                }
              ]
            }
            """;
        return new ByteArrayInputStream(jsonData.getBytes());
    }

    private static List<Category> getExampleCategories() {
        return List.of(
            new Category(1L, "Фрукты", "Различные виды фруктов"),
            new Category(2L, "Овощи", "Разнообразные овощи"));
    }

    private static List<Word> getExampleWords() {
        return List.of(
            new Word(
                1L,
                "Яблоко",
                getExampleCategories().getFirst(),
                "Красный или зелёный фрукт"),
            new Word(
                2L,
                "Морковь",
                getExampleCategories().getLast(),
                "Оранжевый овощ"));
    }
}
