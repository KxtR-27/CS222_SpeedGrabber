package speedgrabber.jsonreaders;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import speedgrabber.records.Leaderboard;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// Tests can be messy. For each test, I purposefully intend to write out the lists.
// This way, I can see exactly what I'm looking at as I unfold the test to inspect.
@SuppressWarnings("ExtractMethodRecommender")
public class LeaderboardReaderTest {
    @BeforeAll
    public static void initializeJsonFiles() throws IOException {
        leaderboardPerGameJson =
                IOUtils.toString(Objects.requireNonNull(LevelReaderTest.class.getResource("sms-anypercent-leaderboard.json")), StandardCharsets.UTF_8);
        leaderboardPerLevelJson =
                IOUtils.toString(Objects.requireNonNull(LevelReaderTest.class.getResource("sms-biancohills-individualworld-leaderboard.json")), StandardCharsets.UTF_8);
    }
    static String leaderboardPerGameJson, leaderboardPerLevelJson;

    @Test
    public void test_creat_pergameCategory() {
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
        Leaderboard actualLeaderboard = LeaderboardReader.create(leaderboardPerGameJson, 3);

        Assertions.assertEquals(expectedLeaderboard, actualLeaderboard);
    }
    @Test
    public void test_create_perlevelCategory() {
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
        Leaderboard actualLeaderboard = LeaderboardReader.create(leaderboardPerLevelJson, 3);

        Assertions.assertEquals(expectedLeaderboard, actualLeaderboard);
    }

    @Test
    public void test_isMaxRunsOutOfBounds() {
        Leaderboard leaderboard = new Leaderboard(
                null, null, null, null, null, 90,
                new ArrayList<>(List.of("a", "b", "c", "d", "e")),
                null, null
        );

        Assertions.assertTrue(LeaderboardReader.isMaxRunsOutOfBounds(leaderboard, 6));
        Assertions.assertFalse(LeaderboardReader.isMaxRunsOutOfBounds(leaderboard, 3));
    }
}
