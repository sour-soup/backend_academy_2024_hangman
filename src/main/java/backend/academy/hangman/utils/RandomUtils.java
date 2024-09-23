package backend.academy.hangman.utils;

import backend.academy.hangman.exception.EmptyCollectionException;
import java.security.SecureRandom;
import java.util.Collection;

public class RandomUtils {
    private static final SecureRandom RANDOM = new SecureRandom();

    private RandomUtils() {
    }

    public static <T> T getRandomElement(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new EmptyCollectionException("Collection must not be null or empty");
        }

        return collection.stream()
            .skip(RANDOM.nextInt(collection.size()))
            .toList()
            .getFirst();
    }
}
