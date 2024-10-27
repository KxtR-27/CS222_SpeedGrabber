package speedgrabber;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import speedgrabber.records.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonReaderTest {
    @BeforeAll
    public static void initializeJsonFiles() throws IOException {
        gameJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-game.json"), StandardCharsets.UTF_8);
        categoryListJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-categories.json"), StandardCharsets.UTF_8);
        levelListJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-levels.json"), StandardCharsets.UTF_8);
        categoryPerGameJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-anypercent-category.json"), StandardCharsets.UTF_8);
        categoryPerLevelJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-individualworld-category.json"), StandardCharsets.UTF_8);
        levelJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-biancohills-level.json"), StandardCharsets.UTF_8);
        leaderboardPerGameJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-anypercent-leaderboard.json"), StandardCharsets.UTF_8);
        leaderboardPerLevelJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-biancohills-individualworld-leaderboard.json"), StandardCharsets.UTF_8);
        run1Json = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-anypercent-run1.json"), StandardCharsets.UTF_8);
        error404Json = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/404-not-found.json"), StandardCharsets.UTF_8);
   }
    static String gameJson;
    static String categoryListJson;
    static String levelListJson;
    static String categoryPerGameJson;
    static String categoryPerLevelJson;
    static String levelJson;
    static String leaderboardPerGameJson;
    static String leaderboardPerLevelJson;
    static String run1Json;
    static String error404Json;

    @Test
    public void test_createGame() {
        List<String> expectedCategoryLinks = List.of(
                "https://www.speedrun.com/api/v1/categories/n2y3r8do",
                "https://www.speedrun.com/api/v1/categories/z27o9gd0",
                "https://www.speedrun.com/api/v1/categories/xk9n9y20",
                "https://www.speedrun.com/api/v1/categories/7kjqlxd3",
                "https://www.speedrun.com/api/v1/categories/wkpmjjkr",
                "https://www.speedrun.com/api/v1/categories/xd1r95wk",
                "https://www.speedrun.com/api/v1/categories/xd14l37d",
                "https://www.speedrun.com/api/v1/categories/w203veo2",
                "https://www.speedrun.com/api/v1/categories/wdm6lm3k",
                "https://www.speedrun.com/api/v1/categories/ndx4ywj2",
                "https://www.speedrun.com/api/v1/categories/xk9e86v2"
        );
        //noinspection ExtractMethodRecommender
        List<String> expectedLevelLinks = List.of(
                "https://www.speedrun.com/api/v1/levels/xd4e80wm",
                "https://www.speedrun.com/api/v1/levels/nwlzepdv",
                "https://www.speedrun.com/api/v1/levels/xd0no09q",
                "https://www.speedrun.com/api/v1/levels/rw6gyn97",
                "https://www.speedrun.com/api/v1/levels/n93l3790",
                "https://www.speedrun.com/api/v1/levels/z985l79l",
                "https://www.speedrun.com/api/v1/levels/rdnxgnwm",
                "https://www.speedrun.com/api/v1/levels/ldyk0jw3",
                "https://www.speedrun.com/api/v1/levels/ywe3pq9l",
                "https://www.speedrun.com/api/v1/levels/69z606d1",
                "https://www.speedrun.com/api/v1/levels/r9gn8qd2",
                "https://www.speedrun.com/api/v1/levels/o9xo069l",
                "https://www.speedrun.com/api/v1/levels/495zn29p",
                "https://www.speedrun.com/api/v1/levels/rdqoqmwx"
        );

        Game expectedGame = new Game(
                "https://www.speedrun.com/sms",
                "https://www.speedrun.com/api/v1/games/v1pxjz68",
                "v1pxjz68",
                "sms",
                "Super Mario Sunshine",

                expectedCategoryLinks,
                expectedLevelLinks
        );
        Game actualGame = JsonReader.createGame(gameJson, categoryListJson, levelListJson);

        Assertions.assertEquals(expectedGame, actualGame);
    }

    @Test
    public void test_createLevel() {
        Level expectedLevel = new Level(
                "https://www.speedrun.com/sms/Bianco_Hills",
                "https://www.speedrun.com/api/v1/levels/xd4e80wm",
                "xd4e80wm",
                "Bianco Hills",

                "https://www.speedrun.com/api/v1/games/v1pxjz68"
        );
        Level actualLevel = JsonReader.createLevel(levelJson);

        Assertions.assertEquals(expectedLevel, actualLevel);
    }

    @Test
    public void test_createCategory_perGame() {
        Category expectedCategory = new Category(
                "https://www.speedrun.com/sms#Any",
                "https://www.speedrun.com/api/v1/categories/n2y3r8do",
                "n2y3r8do",
                "Any%",

                "https://www.speedrun.com/api/v1/leaderboards/v1pxjz68/category/n2y3r8do",
                "https://www.speedrun.com/api/v1/games/v1pxjz68",

                "per-game"
        );
        Category actualCategory = JsonReader.createCategory(categoryPerGameJson);

        Assertions.assertEquals(expectedCategory, actualCategory);
    }
    @Test
    public void test_createCategory_perLevel() {
        Category expectedCategory = new Category(
                "https://www.speedrun.com/sms",
                "https://www.speedrun.com/api/v1/categories/xd1r95wk",
                "xd1r95wk",
                "Individual World",

                null,
                "https://www.speedrun.com/api/v1/games/v1pxjz68",

                "per-level"
        );
        Category actualCategory = JsonReader.createCategory(categoryPerLevelJson);

        Assertions.assertEquals(expectedCategory, actualCategory);
    }

    @Test
    public void test_createLeaderboard_pergameCategory() {
        ArrayList<String> expectedRunLinks = new ArrayList<>(Arrays.asList(
                "https://www.speedrun.com/api/v1/runs/mrx0238m",
                "https://www.speedrun.com/api/v1/runs/megl1l9y",
                "https://www.speedrun.com/api/v1/runs/yoxx36dy"
        ));
        ArrayList<Integer> expectedRunPlaces = new ArrayList<>(Arrays.asList(1, 2, 3));

        Leaderboard expectedLeaderboard = new Leaderboard(
                "https://www.speedrun.com/sms#Any",

                "https://www.speedrun.com/api/v1/categories/n2y3r8do",
                null,
                "https://www.speedrun.com/api/v1/games/v1pxjz68",

                "realtime",
                20,
                expectedRunLinks,
                expectedRunPlaces
        );
        Leaderboard actualLeaderboard = JsonReader.createLeaderboard(leaderboardPerGameJson, 3);

        Assertions.assertEquals(expectedLeaderboard, actualLeaderboard);
    }
    @Test
    public void test_createLeaderboard_perlevelCategory() {
        ArrayList<String> expectedRunLinks = new ArrayList<>(Arrays.asList(
                "https://www.speedrun.com/api/v1/runs/yll5drky",
                "https://www.speedrun.com/api/v1/runs/ywxvw13m",
                "https://www.speedrun.com/api/v1/runs/y805rvxm"
        ));
        //noinspection ExtractMethodRecommender
        ArrayList<Integer> expectedRunPlaces = new ArrayList<>(Arrays.asList(1, 2, 3));

        Leaderboard expectedLeaderboard = new Leaderboard(
                "https://www.speedrun.com/sms/Bianco_Hills#Individual_World",

                "https://www.speedrun.com/api/v1/categories/xd1r95wk",
                "https://www.speedrun.com/api/v1/levels/xd4e80wm",
                "https://www.speedrun.com/api/v1/games/v1pxjz68",

                "realtime",
                20,
                expectedRunLinks,
                expectedRunPlaces
        );
        Leaderboard actualLeaderboard = JsonReader.createLeaderboard(leaderboardPerLevelJson, 3);

        Assertions.assertEquals(expectedLeaderboard, actualLeaderboard);
    }

    @Test
    public void test_createRun() {
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
        Run actualRun = JsonReader.createRun(run1Json, 1);

        Assertions.assertEquals(expectedRun, actualRun);
    }

    @Test
    public void test_populateLeaderboard() {
        //noinspection ExtractMethodRecommender
        ArrayList<String> expectedRunLinks = new ArrayList<>(Arrays.asList(
                String.format("https://www.speedrun.com/api/v1/runs/%s", "mrx0238m"),
                String.format("https://www.speedrun.com/api/v1/runs/%s", "megl1l9y"),
                String.format("https://www.speedrun.com/api/v1/runs/%s", "yoxx36dy")
        ));
        ArrayList<Integer> expectedRunPlaces = new ArrayList<>(Arrays.asList(1, 2, 3));

        Leaderboard expectedLeaderboard = new Leaderboard(
                "https://www.speedrun.com/sms#Any",

                "https://www.speedrun.com/api/v1/categories/n2y3r8do",
                null,
                "https://www.speedrun.com/api/v1/games/v1pxjz68",

                "realtime",
                20,
                expectedRunLinks,
                expectedRunPlaces
        );
        Leaderboard actualLeaderboard = JsonReader.createLeaderboard(leaderboardPerGameJson, 3);
        Assertions.assertEquals(expectedLeaderboard, actualLeaderboard);

        expectedLeaderboard.runLinks().add(String.format("https://www.speedrun.com/api/v1/runs/%s", "mrxlrdgm"));
        expectedLeaderboard.runLinks().add(String.format("https://www.speedrun.com/api/v1/runs/%s", "m7oo6n4y"));
        expectedLeaderboard.runPlaces().add(4);
        expectedLeaderboard.runPlaces().add(5);

        JsonReader.populateLeaderboard(actualLeaderboard, 5, leaderboardPerGameJson);
        Assertions.assertEquals(expectedLeaderboard, actualLeaderboard);
    }

    @Test
    public void test_jsonContains404Error() {
        Assertions.assertTrue(JsonReader.jsonContains404Error(error404Json));
        Assertions.assertFalse(JsonReader.jsonContains404Error(gameJson));
    }
}
