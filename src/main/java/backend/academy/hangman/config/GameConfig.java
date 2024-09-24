package backend.academy.hangman.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:hangman/config.properties")
public interface GameConfig extends Config {
    @Key("app.name")
    @DefaultValue("Hangman")
    String appName();

    @Key("app.version")
    @DefaultValue("unknown")
    String appVersion();

    @Key("max.attempts")
    @DefaultValue("5")
    int maxAttempts();

    @Key("board.start.part")
    @DefaultValue("0.1")
    double boardStartPart();
}
