package speedgrabber.jsonreaders;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import speedgrabber.records.Category;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class CategoryReaderTest {
    @BeforeAll
    public static void initializeJsonFiles() throws IOException {
        categoryPerGameJson =
                IOUtils.toString(Objects.requireNonNull(CategoryReaderTest.class.getResource("sms-anypercent-category.json")), StandardCharsets.UTF_8);
        categoryPerLevelJson =
                IOUtils.toString(Objects.requireNonNull(CategoryReaderTest.class.getResource("sms-individualworld-category.json")), StandardCharsets.UTF_8);
    }
    static String categoryPerGameJson, categoryPerLevelJson;

    @Test
    public void test_create_perGame() {
        Category expectedCategory = new Category(
                "https://www.speedrun.com/sms#Any",
                "https://www.speedrun.com/api/v1/categories/n2y3r8do",
                "n2y3r8do",
                "Any%",

                "https://www.speedrun.com/api/v1/leaderboards/v1pxjz68/category/n2y3r8do",
                "https://www.speedrun.com/api/v1/games/v1pxjz68",

                "per-game"
        );
        Category actualCategory = CategoryReader.create(categoryPerGameJson);

        Assertions.assertEquals(expectedCategory, actualCategory);
    }
    @Test
    public void test_create_perLevel() {
        Category expectedCategory = new Category(
                "https://www.speedrun.com/sms",
                "https://www.speedrun.com/api/v1/categories/xd1r95wk",
                "xd1r95wk",
                "Individual World",

                null,
                "https://www.speedrun.com/api/v1/games/v1pxjz68",

                "per-level"
        );
        Category actualCategory = CategoryReader.create(categoryPerLevelJson);

        Assertions.assertEquals(expectedCategory, actualCategory);
    }
}
