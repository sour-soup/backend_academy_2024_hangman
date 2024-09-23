package backend.academy.hangman.utils;

import backend.academy.hangman.exception.InvalidCollectionRequestException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RandomUtils {
    private static final SecureRandom RANDOM = new SecureRandom();

    private RandomUtils() {
    }

    public static <T> T getRandomElement(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new InvalidCollectionRequestException("Collection must not be null or empty");
        }

        return collection.stream()
            .skip(RANDOM.nextInt(collection.size()))
            .toList()
            .getFirst();
    }

    public static <T> List<T> getRandomElements(Collection<T> collection, long n) {
        if (collection == null) {
            throw new InvalidCollectionRequestException("Collection must not be null");
        }
        if (n < 0 || n > collection.size()) {
            throw new InvalidCollectionRequestException("Invalid number of elements requested");
        }

        List<T> elements = new ArrayList<>(collection);
        Collections.shuffle(elements, RANDOM);

        return elements.stream().limit(n).toList();
    }
}
