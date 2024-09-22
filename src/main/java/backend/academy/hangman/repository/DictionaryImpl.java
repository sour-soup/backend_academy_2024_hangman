package backend.academy.hangman.repository;

import backend.academy.hangman.exception.DictionaryLoadingException;
import backend.academy.hangman.model.Category;
import backend.academy.hangman.model.Word;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DictionaryImpl implements Dictionary {
    private static final String LOAD_ERROR_MESSAGE = "Failed to load dictionary data: ";

    private Map<Long, Category> categories;
    private Map<Long, Word> words;

    public DictionaryImpl() {
        categories = new HashMap<>();
        words = new HashMap<>();
    }

    public void loadDictionaryDefaultData() {
        loadDictionaryData(getClass().getClassLoader()
            .getResourceAsStream("repository/dictionary.json"));
    }

    public void loadDictionaryData(InputStream inputStream) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            DictionaryData data = objectMapper.readValue(inputStream, DictionaryData.class);

            categories = data.categories().stream()
                .collect(Collectors.toMap(Category::id, Function.identity()));

            words = data.words().stream()
                .map(wordData -> new Word(
                    wordData.id(),
                    wordData.name(),
                    categories.get(wordData.categoryId()),
                    wordData.hint()))
                .collect(Collectors.toMap(Word::id, Function.identity()));
        } catch (IOException e) {
            throw new DictionaryLoadingException(LOAD_ERROR_MESSAGE + e.getMessage(), e);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return categories.values().stream().toList();
    }

    @Override
    public List<Word> getAllWords() {
        return words.values().stream().toList();
    }

    @Override
    public List<Word> getWordsByCategory(Category category) {
        return words.values().stream()
            .filter(word -> Objects.equals(word.category().id(), category.id()))
            .toList();
    }
}
