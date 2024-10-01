package backend.academy.hangman.game;

public interface Board {
    void loadAsciiArt(String asciiArt);

    void loadDefaultAsciiArt();

    String getAsciiArt();

    void addAttempt();
}
