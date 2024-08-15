package backend.academy.samples.lombok;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.Nullable;

@Log4j2
@RequiredArgsConstructor
public class CalcService {
    private final String stringValue;

    @Getter
    private @Nullable Integer integerValue;
    @Getter
    private Double doubleValue;

    private void calculateDoubleValue() {
        log.info("Calculating double value");
        double a = 2.0;
        for (int i = 0; i < 9; i++) {
            a = a * 1.5;
        }
        doubleValue = a;
    }

    public static void main(String[] args) {
        var service = new CalcService("1.0");
        service.calculateDoubleValue();

        log.info(service.doubleValue());
    }
}
