package backend.academy.hangman.utils;

import backend.academy.hangman.exception.EmptyCollectionException;
import java.util.Collection;
import java.util.Random;

public class RandomUtils {
    private static final Random RANDOM = new Random();

    public static <T> T getRandomElement(Collection<T> collection){
        if (collection == null || collection.isEmpty()){
            throw new EmptyCollectionException("Collection must not be null or empty");
        }

        return collection.stream()
            .skip(RANDOM.nextInt(collection.size()))
            .toList()
            .getFirst();
    }
}
