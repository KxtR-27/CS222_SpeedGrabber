package speedgrabber.jsonreaders;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import speedgrabber.SGUtils;
import speedgrabber.records.Run;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class RunReaderTest {
    @BeforeAll
    public static void initializeJsonFile() throws IOException {
        runJson = IOUtils.toString(Objects.requireNonNull(RunReaderTest.class.getResource("sms-anypercent-run1.json")), StandardCharsets.UTF_8);
    }
    static String runJson;

    @Test
    public void test_create() {
        Run expectedRun = new Run(
                "https://www.speedrun.com/sms/run/mrx0238m",
                "https://www.speedrun.com/api/v1/runs/mrx0238m",
                "mrx0238m",

                List.of("https://www.speedrun.com/api/v1/users/kjprmwk8"),
                "https://www.speedrun.com/api/v1/categories/n2y3r8do",
                "https://www.speedrun.com/api/v1/games/v1pxjz68",

                1,
                SGUtils.asLocalDate("2024-09-26"),
                SGUtils.asLocalDateTime("2024-10-14T08:11:12Z"),
                SGUtils.asLocalTime("PT17M24S")
        );
        Run actualRun = RunReader.create(runJson, 1);

        Assertions.assertEquals(expectedRun, actualRun);
    }
}
