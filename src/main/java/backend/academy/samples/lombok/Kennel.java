package backend.academy.samples.lombok;

import java.util.Set;
import lombok.Builder;
import lombok.Singular;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;

@Log4j2
@Builder
@ToString
public class Kennel {
    private @NonNull Integer id;
    private String name;
    @Singular
    private Set<Dog> dogs;

    record Dog(
        @NonNull Integer id,
        @NonNull String name,
        @NonNull String breed
    ) {
    }

    public static void main(String[] args) {
        Kennel kennel = Kennel.builder()
            .id(1)
            .name("Bazz")
            .dog(new Dog(1, "Foo", "Bar"))
            .build();

        log.info(kennel);
    }
}
