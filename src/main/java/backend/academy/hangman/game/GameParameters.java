package backend.academy.hangman.game;

import backend.academy.hangman.model.Word;

public record GameParameters(Word word, int maxAttempts) {
}
