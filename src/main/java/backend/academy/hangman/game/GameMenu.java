package backend.academy.hangman.game;

import java.util.Optional;

public interface GameMenu {
    Optional<GameParameters> run();
}
