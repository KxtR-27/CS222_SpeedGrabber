package speedgrabber.jsonreaders;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import speedgrabber.records.Game;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class GameReaderTest {
    @BeforeAll
    public static void initializeJsonFiles() throws IOException {
        gameJson =
                IOUtils.toString(Objects.requireNonNull(GameReaderTest.class.getResource("sms-game.json")), StandardCharsets.UTF_8);
        categoriesJson =
                IOUtils.toString(Objects.requireNonNull(GameReaderTest.class.getResource("sms-categories.json")), StandardCharsets.UTF_8);
        levelsJson =
                IOUtils.toString(Objects.requireNonNull(GameReaderTest.class.getResource("sms-levels.json")), StandardCharsets.UTF_8);
    }
    static String gameJson, categoriesJson, levelsJson;

    @Test
    public void test_create() {
        Game expectedGame = new Game(
                "https://www.speedrun.com/sms",
                "https://www.speedrun.com/api/v1/games/v1pxjz68",
                "v1pxjz68",
                "sms",
                "Super Mario Sunshine",

                List.of(
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
                ),
                List.of(
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
                )
        );
        Game actualGame = GameReader.create(gameJson, categoriesJson, levelsJson);

        Assertions.assertEquals(expectedGame, actualGame);
    }
}
