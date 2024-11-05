package speedgrabber.jsonreaders;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

// Tests aren't made to be pretty. Also, the program thinks the suppression is redundant, which it is not.
@SuppressWarnings({"ExtractMethodRecommender", "RedundantSuppression"})
public class JsonReaderTest {
    @BeforeAll
    public static void initializeJsonFile() throws IOException {
        exampleJson =
                IOUtils.toString(Objects.requireNonNull(JsonReaderTest.class.getResource("example.json")), StandardCharsets.UTF_8);
        error404Json =
                IOUtils.toString(Objects.requireNonNull(JsonReaderTest.class.getResource("error404.json")), StandardCharsets.UTF_8);
    }
    static String exampleJson, error404Json;

    @Test
    public void test_definiteScan() {
        JsonReader.loadJsonDocument(exampleJson);
        Assertions.assertEquals("valueString", JsonReader.definiteScan("keyString"));
    }

    @Test
    public void test_jsonContainsError_404() {
        Assertions.assertEquals(404, JsonReader.checkForKnownErrors(error404Json));
        Assertions.assertNotEquals(404, JsonReader.checkForKnownErrors(exampleJson));
    }
}
