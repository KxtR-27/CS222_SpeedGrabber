package speedgrabber;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import speedgrabber.records.*;
import speedgrabber.records.interfaces.Player;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Tests aren't made to be pretty. Also, the program thinks the suppression is redundant, which it is not.
@SuppressWarnings({"ExtractMethodRecommender", "RedundantSuppression"})
public class JsonReaderTest {
    @BeforeAll
    public static void initializeJsonFiles() throws IOException {
        gameJson =
                IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-game.json"), StandardCharsets.UTF_8);
        categoryListJson =
                IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-categories.json"), StandardCharsets.UTF_8);
        levelListJson =
                IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-levels.json"), StandardCharsets.UTF_8);
        categoryPerGameJson =
                IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-anypercent-category.json"), StandardCharsets.UTF_8);
        categoryPerLevelJson =
                IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-individualworld-category.json"), StandardCharsets.UTF_8);
        levelJson =
                IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-biancohills-level.json"), StandardCharsets.UTF_8);
        leaderboardPerGameJson =
                IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-anypercent-leaderboard.json"), StandardCharsets.UTF_8);
        leaderboardPerLevelJson =
                IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-biancohills-individualworld-leaderboard.json"), StandardCharsets.UTF_8);
        runJson =
                IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/sms-anypercent-run1.json"), StandardCharsets.UTF_8);
        userJson =
                IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/example-user.json"), StandardCharsets.UTF_8);
        guestJson =
                IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/example-guest.json"), StandardCharsets.UTF_8);
        error404Json =
                IOUtils.toString(new FileInputStream("src/test/resources/speedgrabber/404-not-found.json"), StandardCharsets.UTF_8);
   }
    static String
            gameJson, categoryListJson, levelListJson, categoryPerGameJson, categoryPerLevelJson, levelJson,
            leaderboardPerGameJson, leaderboardPerLevelJson, runJson, userJson, guestJson, error404Json;

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
        ArrayList<String[]> expectedPlayerLinks = new ArrayList<>(Arrays.asList(
                new String[]{"https://www.speedrun.com/api/v1/users/kjprmwk8"},
                new String[]{"https://www.speedrun.com/api/v1/users/dx3ml28l"},
                new String[]{"https://www.speedrun.com/api/v1/users/8r3064w8"}
        ));

        Leaderboard expectedLeaderboard = new Leaderboard(
                "https://www.speedrun.com/sms#Any",

                "https://www.speedrun.com/api/v1/categories/n2y3r8do",
                null,
                "https://www.speedrun.com/api/v1/games/v1pxjz68",

                "realtime",
                20,
                expectedRunLinks,
                expectedRunPlaces,
                expectedPlayerLinks
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
        ArrayList<Integer> expectedRunPlaces = new ArrayList<>(Arrays.asList(1, 2, 3));
        ArrayList<String[]> expectedPlayerLinks = new ArrayList<>(Arrays.asList(
                new String[]{"https://www.speedrun.com/api/v1/users/18v69vxl"},
                new String[]{"https://www.speedrun.com/api/v1/users/o86v6k5j"},
                new String[]{"https://www.speedrun.com/api/v1/users/8162y05x"}
        ));

        Leaderboard expectedLeaderboard = new Leaderboard(
                "https://www.speedrun.com/sms/Bianco_Hills#Individual_World",

                "https://www.speedrun.com/api/v1/categories/xd1r95wk",
                "https://www.speedrun.com/api/v1/levels/xd4e80wm",
                "https://www.speedrun.com/api/v1/games/v1pxjz68",

                "realtime",
                20,
                expectedRunLinks,
                expectedRunPlaces,
                expectedPlayerLinks
        );
        Leaderboard actualLeaderboard = JsonReader.createLeaderboard(leaderboardPerLevelJson, 3);

        Assertions.assertEquals(expectedLeaderboard, actualLeaderboard);
    }
    @Test
    public void test_populateLeaderboard() {
        ArrayList<String> expectedRunLinks = new ArrayList<>(Arrays.asList(
                String.format("https://www.speedrun.com/api/v1/runs/%s", "mrx0238m"),
                String.format("https://www.speedrun.com/api/v1/runs/%s", "megl1l9y"),
                String.format("https://www.speedrun.com/api/v1/runs/%s", "yoxx36dy")
        ));
        ArrayList<Integer> expectedRunPlaces = new ArrayList<>(Arrays.asList(1, 2, 3));
        ArrayList<String[]> expectedPlayerLinks = new ArrayList<>(Arrays.asList(
                new String[]{"https://www.speedrun.com/api/v1/users/kjprmwk8"},
                new String[]{"https://www.speedrun.com/api/v1/users/dx3ml28l"},
                new String[]{"https://www.speedrun.com/api/v1/users/8r3064w8"}
        ));

        Leaderboard expectedLeaderboard = new Leaderboard(
                "https://www.speedrun.com/sms#Any",

                "https://www.speedrun.com/api/v1/categories/n2y3r8do",
                null,
                "https://www.speedrun.com/api/v1/games/v1pxjz68",

                "realtime",
                20,
                expectedRunLinks,
                expectedRunPlaces,
                expectedPlayerLinks
        );
        Leaderboard actualLeaderboard = JsonReader.createLeaderboard(leaderboardPerGameJson, 3);
        Assertions.assertEquals(expectedLeaderboard, actualLeaderboard);

        expectedLeaderboard.runlinks().add(String.format("https://www.speedrun.com/api/v1/runs/%s", "mrxlrdgm"));
        expectedLeaderboard.runlinks().add(String.format("https://www.speedrun.com/api/v1/runs/%s", "m7oo6n4y"));
        expectedLeaderboard.runplaces().add(4);
        expectedLeaderboard.runplaces().add(5);

        JsonReader.populateLeaderboard(actualLeaderboard, 5, leaderboardPerGameJson);
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
        Run actualRun = JsonReader.createRun(runJson, 1);

        Assertions.assertEquals(expectedRun, actualRun);
    }

    @Test
    public void test_createUser() {
        User expectedUser = new User(
                "https://www.speedrun.com/user/chewdiggy",
                "https://www.speedrun.com/api/v1/users/kjpdr4jq",
                "kjpdr4jq",
                "chewdiggy",

                "user",
                "https://www.speedrun.com/api/v1/runs?user=kjpdr4jq",
                "https://www.speedrun.com/api/v1/users/kjpdr4jq/personal-bests",

                "https://www.twitch.tv/username",
                "https://www.hitbox.tv/username",
                "https://www.youtube.com/oih2grfwezf782zroufiw",
                "https://www.twitter.com/username",
                "http://www.speedrunslive.com/profiles/#!/username"
        );
        User actualuser = JsonReader.createUser(userJson);

        Assertions.assertEquals(expectedUser, actualuser);
    }
    @Test
    public void test_createGuest() {
        Guest expectedGuest = new Guest(
                "https://www.speedrun.com/api/v1/guests/Alex",
                "Alex",

                "https://www.speedrun.com/api/v1/runs?guest=Alex"
        );
        Guest actualGuest = JsonReader.createGuest(guestJson);

        Assertions.assertEquals(expectedGuest, actualGuest);
    }
    @Test
    public void test_createPlayer() {
        Player playerGuest = JsonReader.createPlayer(guestJson);
        Assertions.assertInstanceOf(Guest.class, playerGuest);

        Player playerUser = JsonReader.createPlayer(userJson);
        Assertions.assertInstanceOf(User.class, playerUser);
    }

    @Test
    public void test_jsonContainsError_404() {
        Assertions.assertEquals(404, JsonReader.checkForKnownErrors(error404Json));
        Assertions.assertNotEquals(404, JsonReader.checkForKnownErrors(gameJson));
    }
}
