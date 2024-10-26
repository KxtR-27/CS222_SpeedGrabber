package speedgrabber;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import speedgrabber.records.Category;
import speedgrabber.records.Game;
import speedgrabber.records.Leaderboard;
import speedgrabber.records.Run;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonReaderTest {
    @BeforeAll
    public static void initializeJsonReaders() throws IOException {
        String gameJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-game.json"), StandardCharsets.UTF_8);
        String categoryListJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-categories.json"), StandardCharsets.UTF_8);
        String leaderboardJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-anypercent-leaderboard.json"), StandardCharsets.UTF_8);

        gameReader = JsonReader.create(gameJson);
        categoryListReader = JsonReader.create(categoryListJson);
        leaderboardReader = JsonReader.create(leaderboardJson);
    }
    static JsonReader gameReader;
    static JsonReader categoryListReader;
    static JsonReader leaderboardReader;

    @Test
    public void test_createGame() {
        Game expectedGame = new Game(
                "https://www.speedrun.com/sms",
                "https://www.speedrun.com/api/v1/games/v1pxjz68",
                "v1pxjz68",
                "Super Mario Sunshine",

                "https://www.speedrun.com/api/v1/games/v1pxjz68/categories"
        );
        Game actualGame = gameReader.createGame();

        Assertions.assertEquals(expectedGame, actualGame);
    }

    @Test
    public void test_createCategoryList() {
        List<Category> expectedCategoryList = List.of(
                new Category("https://www.speedrun.com/sms#Any", "https://www.speedrun.com/api/v1/categories/n2y3r8do", "n2y3r8do", "Any%", "https://www.speedrun.com/api/v1/leaderboards/v1pxjz68/category/n2y3r8do", "https://www.speedrun.com/api/v1/games/v1pxjz68"),
                new Category("https://www.speedrun.com/sms#120_Shines", "https://www.speedrun.com/api/v1/categories/z27o9gd0", "z27o9gd0", "120 Shines", "https://www.speedrun.com/api/v1/leaderboards/v1pxjz68/category/z27o9gd0", "https://www.speedrun.com/api/v1/games/v1pxjz68"),
                new Category("https://www.speedrun.com/sms#96_Shines", "https://www.speedrun.com/api/v1/categories/xk9n9y20", "xk9n9y20", "96 Shines", "https://www.speedrun.com/api/v1/leaderboards/v1pxjz68/category/xk9n9y20", "https://www.speedrun.com/api/v1/games/v1pxjz68"),
                new Category("https://www.speedrun.com/sms#79_Shines", "https://www.speedrun.com/api/v1/categories/7kjqlxd3", "7kjqlxd3", "79 Shines", "https://www.speedrun.com/api/v1/leaderboards/v1pxjz68/category/7kjqlxd3", "https://www.speedrun.com/api/v1/games/v1pxjz68"),
                new Category("https://www.speedrun.com/sms#All_Episodes", "https://www.speedrun.com/api/v1/categories/wkpmjjkr", "wkpmjjkr", "All Episodes", "https://www.speedrun.com/api/v1/leaderboards/v1pxjz68/category/wkpmjjkr", "https://www.speedrun.com/api/v1/games/v1pxjz68"),
                new Category("https://www.speedrun.com/sms#Any_Hoverless", "https://www.speedrun.com/api/v1/categories/xd14l37d", "xd14l37d", "Any% Hoverless", "https://www.speedrun.com/api/v1/leaderboards/v1pxjz68/category/xd14l37d", "https://www.speedrun.com/api/v1/games/v1pxjz68"),
                new Category("https://www.speedrun.com/sms#All_Blue_Coins", "https://www.speedrun.com/api/v1/categories/ndx4ywj2", "ndx4ywj2", "All Blue Coins", "https://www.speedrun.com/api/v1/leaderboards/v1pxjz68/category/ndx4ywj2", "https://www.speedrun.com/api/v1/games/v1pxjz68"),
                new Category("https://www.speedrun.com/sms#20_Shines", "https://www.speedrun.com/api/v1/categories/xk9e86v2", "xk9e86v2", "20 Shines", "https://www.speedrun.com/api/v1/leaderboards/v1pxjz68/category/xk9e86v2", "https://www.speedrun.com/api/v1/games/v1pxjz68")
        );
        List<Category> actualCategoryList = categoryListReader.createCategoryList();

        Assertions.assertEquals(expectedCategoryList, actualCategoryList);
    }

    @Test
    public void test_createLeaderboard() throws IOException {
        List<String> expectedRunLinks = List.of(
                "https://www.speedrun.com/api/v1/runs/mrx0238m",
                "https://www.speedrun.com/api/v1/runs/megl1l9y",
                "https://www.speedrun.com/api/v1/runs/yoxx36dy"
        );
        List<Integer> expectedRunPlaces = List.of(
                1, 2, 3
        );

        Leaderboard expectedLeaderboard = new Leaderboard(
                "https://www.speedrun.com/sms#Any",

                "https://www.speedrun.com/api/v1/games/v1pxjz68",
                "https://www.speedrun.com/api/v1/categories/n2y3r8do",

                "realtime",
                expectedRunLinks,
                expectedRunPlaces
        );
        Leaderboard actualLeaderboard = leaderboardReader.createLeaderboard(3);

        Assertions.assertEquals(expectedLeaderboard, actualLeaderboard);
    }
}
