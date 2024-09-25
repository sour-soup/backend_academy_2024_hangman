package backend.academy.hangman.repository;

import backend.academy.hangman.model.Category;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryData {
    private List<Category> categories;
    private List<WordData> words;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WordData {
        private Long id;
        private String name;
        private Long categoryId;
        private String hint;
    }
}
