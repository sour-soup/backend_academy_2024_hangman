package backend.academy.hangman.model;

public record Word(Long id, String name, Category category, String hint) {
}
