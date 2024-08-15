package backend.academy.samples.logging;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ErrorLogging {
    public static void main(String[] args) {
        try {
            getData();
        } catch (IllegalArgumentException e) {
            log.error("unexpected error", e);
        }

    }

    static void getData() throws IllegalArgumentException {
        throw new IllegalArgumentException("Sorry IllegalArgumentException!");
    }

}
