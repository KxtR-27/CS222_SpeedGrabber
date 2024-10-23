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
import java.util.LinkedHashMap;
import java.util.List;

public class JsonReaderTest {
    @BeforeAll
    public static void initializeJsonReaders() throws IOException {
        String gameJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/records/sms-game.json"), StandardCharsets.UTF_8);
        String categoryListJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/records/sms-categories.json"), StandardCharsets.UTF_8);
        String leaderboardJson = IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/records/sms-anypercent-leaderboard.json"), StandardCharsets.UTF_8);

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

    // This method also tests createRun()
    @Test
    public void test_createLeaderboard() throws IOException {
        LinkedHashMap<Integer, Run> expectedRuns = new LinkedHashMap<>();
        expectedRuns.put(1, new Run("https://www.speedrun.com/sms/run/zppv46rz", "https://www.speedrun.com/api/v1/runs/zppv46rz", "zppv46rz", "https://www.speedrun.com/api/v1/games/v1pxjz68", "https://www.speedrun.com/api/v1/categories/z27o9gd0", List.of("https://www.speedrun.com/api/v1/users/98r1n2qj"), "2024-01-21T12:20:16Z", "PT2H51M34S"));
        expectedRuns.put(2, new Run("https://www.speedrun.com/sms/run/megl1l9y", "https://www.speedrun.com/api/v1/runs/megl1l9y", "megl1l9y", "https://www.speedrun.com/api/v1/games/v1pxjz68", "https://www.speedrun.com/api/v1/categories/n2y3r8do", List.of("https://www.speedrun.com/api/v1/users/dx3ml28l"), "2024-09-17T02:51:23Z", "PT23M57S"));
        expectedRuns.put(3, new Run("https://www.speedrun.com/sms/run/yoxx36dy", "https://www.speedrun.com/api/v1/runs/yoxx36dy", "yoxx36dy", "https://www.speedrun.com/api/v1/games/v1pxjz68", "https://www.speedrun.com/api/v1/categories/n2y3r8do", List.of("https://www.speedrun.com/api/v1/users/8r3064w8"), "2024-10-05T05:59:20Z", "PT24M45S"));

        Leaderboard expectedLeaderboard = new Leaderboard(
                "https://www.speedrun.com/sms#120_Shines",

                "https://www.speedrun.com/api/v1/games/v1pxjz68",
                "https://www.speedrun.com/api/v1/categories/z27o9gd0",

                "realtime",
                expectedRuns
        );
        Leaderboard actualLeaderboard = leaderboardReader.test_createLeaderboard();

        Assertions.assertEquals(expectedLeaderboard, actualLeaderboard);
    }
}
