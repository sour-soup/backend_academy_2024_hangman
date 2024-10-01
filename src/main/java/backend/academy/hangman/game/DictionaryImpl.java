package backend.academy.hangman.game;

import backend.academy.hangman.exception.ResourceLoadingException;
import backend.academy.hangman.model.Category;
import backend.academy.hangman.model.DictionaryData;
import backend.academy.hangman.model.Word;
import backend.academy.hangman.utils.RandomUtils;
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

    public void loadDefaultDictionaryData() {
        try (
            InputStream defaultArtStream = getClass().getClassLoader()
                .getResourceAsStream("hangman/dictionary.json")) {
            loadDictionaryData(Objects.requireNonNull(defaultArtStream));
        } catch (IOException e) {
            throw new ResourceLoadingException(LOAD_ERROR_MESSAGE + e.getMessage(), e);
        }
    }

    public void loadDictionaryData(InputStream inputStream) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            DictionaryData data = objectMapper.readValue(inputStream, DictionaryData.class);

            categories = data.categories().stream().collect(Collectors.toMap(Category::id, Function.identity()));

            words = data.words().stream().map(
                wordData -> new Word(wordData.id(), wordData.name(), categories.get(wordData.categoryId()),
                    wordData.hint())).collect(Collectors.toMap(Word::id, Function.identity()));
        } catch (IOException e) {
            throw new ResourceLoadingException(LOAD_ERROR_MESSAGE + e.getMessage(), e);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return categories.values().stream().toList();
    }

    @Override
    public Word getRandomWord() {
        List<Word> wordsList = words.values().stream().toList();
        return RandomUtils.getRandomElement(wordsList);
    }

    @Override
    public Word getRandomWordByCategory(Category category) {
        List<Word> wordList = words.values().stream()
            .filter(word -> Objects.equals(word.category().id(), category.id()))
            .toList();
        return RandomUtils.getRandomElement(wordList);
    }
}
