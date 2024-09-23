package backend.academy.hangman.service;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.model.Category;
import backend.academy.hangman.model.Word;
import backend.academy.hangman.repository.Dictionary;
import backend.academy.hangman.utils.RandomUtils;
import java.util.List;

public class DictionaryServiceImpl implements DictionaryService {
    @Override
    public List<Category> getAllCategories() {
        Dictionary dictionary = DependencyResolver.getInstance().resolve(Dictionary.class);
        return dictionary.getAllCategories();
    }

    @Override
    public Word getRandomWord() {
        Dictionary dictionary = DependencyResolver.getInstance().resolve(Dictionary.class);
        return RandomUtils.getRandomElement(dictionary.getAllWords());
    }

    @Override
    public Word getRandomWordByCategory(Category category) {
        Dictionary dictionary = DependencyResolver.getInstance().resolve(Dictionary.class);
        return RandomUtils.getRandomElement(dictionary.getWordsByCategory(category));
    }
}
