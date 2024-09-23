package backend.academy.hangman.repository;

import backend.academy.hangman.exception.DictionaryLoadingException;
import backend.academy.hangman.model.Category;
import backend.academy.hangman.model.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DictionaryImplTest {
    private DictionaryImpl dictionary;

    @BeforeEach
    void setUp() throws DictionaryLoadingException {
        dictionary = new DictionaryImpl();
        dictionary.loadDictionaryData(getExampleInputStream());
    }

    @Test
    void loadDictionaryData_ShouldLoadDictionaryDataCorrectly() throws DictionaryLoadingException {
        // Act
        dictionary.loadDictionaryData(getExampleInputStream());

        // Assert
        List<Category> actualCategories = dictionary.getAllCategories();
        List<Word> actualWords = dictionary.getAllWords();

        assertThat(actualCategories).isEqualTo(getExampleCategories());
        assertThat(actualWords).isEqualTo(getExampleWords());
    }

    @Test
    void loadDictionaryData_ShouldThrowException_WhenJsonIsInvalid() {
        // Arrange
        String invalidJsonData = "baobab";
        InputStream inputStream = new ByteArrayInputStream(invalidJsonData.getBytes());

        // Act & Assert
        assertThatThrownBy(() -> dictionary.loadDictionaryData(inputStream))
            .isInstanceOf(DictionaryLoadingException.class);
    }

    @Test
    void loadDictionaryData_ShouldThrowException_WhenInputStreamIsNull() {
        // Arrange (Given)
        InputStream inputStream = InputStream.nullInputStream();

        // Act & Assert (When & Then)
        assertThatThrownBy(() -> dictionary.loadDictionaryData(inputStream))
            .isInstanceOf(DictionaryLoadingException.class);
    }

    @Test
    void getAllCategories() {
        // Act
        List<Category> actualCategories = dictionary.getAllCategories();

        // Assert
        assertThat(actualCategories).containsExactlyInAnyOrderElementsOf(getExampleCategories());
    }

    @Test
    void getAllWords() {
        // Act
        List<Word> actualWords = dictionary.getAllWords();

        // Assert
        assertThat(actualWords).containsExactlyInAnyOrderElementsOf(getExampleWords());
    }

    @Test
    void getWordsByCategory() {
        // Act
        Category category = dictionary.getAllCategories().getFirst();
        List<Word> actualWords = dictionary.getWordsByCategory(category);

        List<Word> expectedWords = getExampleWords().stream()
            .filter(word -> Objects.equals(word.category().id(), category.id()))
            .toList();

        // Assert
        assertThat(actualWords).isEqualTo(expectedWords);
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
