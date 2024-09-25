package backend.academy.hangman.repository;

import backend.academy.hangman.model.Category;
import backend.academy.hangman.model.Word;
import java.util.List;

public interface Dictionary {
    List<Category> getAllCategories();

    List<Word> getAllWords();

    List<Word> getWordsByCategory(Category category);
}
