package backend.academy.hangman.game;

import backend.academy.hangman.model.Category;
import backend.academy.hangman.model.Word;
import java.util.List;

public interface Dictionary {
    List<Category> getAllCategories();

    Word getRandomWord();

    Word getRandomWordByCategory(Category category);
}
