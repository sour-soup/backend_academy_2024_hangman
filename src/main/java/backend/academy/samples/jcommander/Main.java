package backend.academy.samples.jcommander;

import com.beust.jcommander.JCommander;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {
    public static void main(String[] args) {
        String[] sampleArgs = { "-log", "2", "-groups", "unit" };
        CliParams params = new CliParams();
        JCommander.newBuilder()
            .addObject(params)
            .build()
            .parse(sampleArgs);
        log.info(params);
    }
}
