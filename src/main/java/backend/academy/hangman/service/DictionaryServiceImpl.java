package backend.academy.hangman.service;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.model.Category;
import backend.academy.hangman.model.Word;
import backend.academy.hangman.repository.Dictionary;
import backend.academy.hangman.utils.RandomUtils;
import java.util.List;

public class DictionaryServiceImpl implements DictionaryService {
    private final Dictionary dictionary;

    public DictionaryServiceImpl() {
        dictionary = DependencyResolver.getInstance().resolve(Dictionary.class);
    }

    @Override
    public List<Category> getAllCategories() {
        return dictionary.getAllCategories();
    }

    @Override
    public Word getRandomWord() {
        return RandomUtils.getRandomElement(dictionary.getAllWords());
    }

    @Override
    public Word getRandomWordByCategory(Category category) {
        return RandomUtils.getRandomElement(dictionary.getWordsByCategory(category));
    }
}
