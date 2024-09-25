package backend.academy.hangman;

import backend.academy.hangman.config.DependencyResolver;
import backend.academy.hangman.config.GameConfig;
import backend.academy.hangman.game.HangmanGame;
import backend.academy.hangman.interaction.GameInputConsoleHandler;
import backend.academy.hangman.interaction.GameInputHandler;
import backend.academy.hangman.interaction.GameOutputConsoleHandler;
import backend.academy.hangman.interaction.GameOutputHandler;
import backend.academy.hangman.repository.Dictionary;
import backend.academy.hangman.repository.DictionaryImpl;
import backend.academy.hangman.service.DictionaryService;
import backend.academy.hangman.service.DictionaryServiceImpl;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigFactory;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        registerDependencies();
        HangmanGame hangmanGame = new HangmanGame();
        hangmanGame.run();
    }

    private static void registerDependencies() {
        DependencyResolver dependencyResolver = DependencyResolver.getInstance();
        dependencyResolver.register(InputStream.class, () -> System.in);
        dependencyResolver.register(OutputStream.class, () -> System.out);
        dependencyResolver.register(GameInputHandler.class, GameInputConsoleHandler::new);
        dependencyResolver.register(GameOutputHandler.class, GameOutputConsoleHandler::new);
        dependencyResolver.register(GameConfig.class, () -> ConfigFactory.create(GameConfig.class));
        dependencyResolver.register(DictionaryService.class, DictionaryServiceImpl::new);
        dependencyResolver.register(Dictionary.class, () -> {
            DictionaryImpl dictionary = new DictionaryImpl();
            dictionary.loadDefaultDictionaryData();
            return dictionary;
        });
    }
}