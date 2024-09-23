package backend.academy.hangman.service;

import backend.academy.hangman.model.Category;
import backend.academy.hangman.model.Word;
import java.util.List;

public interface DictionaryService {
    List<Category> getAllCategories();

    Word getRandomWord();

    Word getRandomWordByCategory(Category category);
}
