package speedgrabber.jsonreaders;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import speedgrabber.records.Level;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class LevelReaderTest {
    @BeforeAll
    public static void initializeJsonFile() throws IOException {
        levelJson =
                IOUtils.toString(Objects.requireNonNull(LevelReaderTest.class.getResource("sms-biancohills-level.json")), StandardCharsets.UTF_8);
    }
    static String levelJson;

    @Test
    public void test_create() {
        Level expectedLevel = new Level(
                "https://www.speedrun.com/sms/Bianco_Hills",
                "https://www.speedrun.com/api/v1/levels/xd4e80wm",
                "xd4e80wm",
                "Bianco Hills",

                "https://www.speedrun.com/api/v1/games/v1pxjz68"
        );
        Level actualLevel = LevelReader.create(levelJson);

        Assertions.assertEquals(expectedLevel, actualLevel);
    }
}
