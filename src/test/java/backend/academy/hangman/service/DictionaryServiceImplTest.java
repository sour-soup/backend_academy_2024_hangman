package backend.academy.hangman.service;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.exception.InvalidCollectionRequestException;
import backend.academy.hangman.model.Category;
import backend.academy.hangman.model.Word;
import backend.academy.hangman.repository.Dictionary;
import backend.academy.hangman.utils.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DictionaryServiceImplTest {
    private Dictionary mockDictionary;
    private DictionaryServiceImpl dictionaryService;

    @BeforeEach
    void setUp() {
        DependencyResolver.getInstance().clear();

        mockDictionary = Mockito.mock(Dictionary.class);
        DependencyResolver.getInstance()
            .register(Dictionary.class, () -> mockDictionary);
        dictionaryService = new DictionaryServiceImpl();
    }

    @Test
    void getAllCategories() {
        // Arrange
        List<Category> expectedCategories = List.of(
            new Category(1L, "Фрукты", "Различные виды фруктов"),
            new Category(2L, "Овощи", "Разнообразные овощи"));
        when(mockDictionary.getAllCategories()).thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = dictionaryService.getAllCategories();

        // Assert
        assertThat(actualCategories).containsExactlyInAnyOrderElementsOf(expectedCategories);
    }

    @Test
    void getRandomWord() {
        // Arrange
        List<Word> words = List.of(
            new Word(1L, "Яблоко", null, "Красный или зелёный фрукт"),
            new Word(2L, "Морковь", null, "Оранжевый овощ"));

        when(mockDictionary.getAllWords()).thenReturn(words);
        try (var randomUtilsMock = mockStatic(RandomUtils.class)) {
            randomUtilsMock.when(RandomUtils.getRandomElement(any()))
                .thenReturn(words.getFirst());

            // Act
            Word randomWord = dictionaryService.getRandomWord();

            // Assert
            assertThat(randomWord).isEqualTo(words.getFirst());
            verify(mockDictionary).getAllWords();
        }
    }

    @Test
    void getRandomWordByCategory() {
        // Arrange
        Category category = new Category(1L, "Фрукты", "Различные виды фруктов");
        List<Word> words = List.of(
            new Word(1L, "Яблоко", null, "Красный или зелёный фрукт"),
            new Word(2L, "Морковь", null, "Оранжевый овощ"));

        when(mockDictionary.getWordsByCategory(category)).thenReturn(words);
        try (var randomUtilsMock = mockStatic(RandomUtils.class)) {
            randomUtilsMock.when(RandomUtils.getRandomElement(any()))
                .thenReturn(words.getFirst());

            // Act
            Word randomWord = dictionaryService.getRandomWordByCategory(category);

            // Assert
            assertThat(randomWord).isEqualTo(words.getFirst());
            verify(mockDictionary).getWordsByCategory(category);
        }
    }

    @Test
    void getRandomWordByCategory_ShouldThrowException_WhenCategoryIsEmpty() {
        // Arrange
        Category category = new Category(1L, "Фрукты", "Различные виды фруктов");

        when(mockDictionary.getWordsByCategory(category)).thenReturn(List.of());
        try (var randomUtilsMock = mockStatic(RandomUtils.class)) {
            randomUtilsMock.when(RandomUtils.getRandomElement(List.of()))
                .thenThrow(new InvalidCollectionRequestException("Collection must not be null or empty"));

            // Act & Assert
            assertThatThrownBy(() -> dictionaryService.getRandomWordByCategory(category))
                .isInstanceOf(InvalidCollectionRequestException.class);
            verify(mockDictionary).getWordsByCategory(category);
        }
    }
}
