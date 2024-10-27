package speedgrabber;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import speedgrabber.records.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class IdentifiableTest {
    @Test
    public void test_identify_game() {
        Identifiable identifiable = new Game(
                "webLink",
                "selfLink",
                "id",
                "abbreviation",
                "name",

                new ArrayList<>(),
                new ArrayList<>()
        );

        String expectedIdentity = "id+abbreviation";
        String actualIdentity = identifiable.identify();

        Assertions.assertEquals(expectedIdentity, actualIdentity);
    }

    @Test
    public void test_identify_level() {
        Identifiable identifiable = new Level(
                "webLink",
                "selfLink",
                "id",
                "name",

                "gameLink"
        );

        String expectedIdentity = "selfLink";
        String actualIdentity = identifiable.identify();

        Assertions.assertEquals(expectedIdentity, actualIdentity);
    }

    @Test
    public void test_identify_category() {
        Identifiable identifiable = new Category(
                "webLink",
                "selfLink",
                "id",
                "name",

                "leaderboard",
                "game",

                "type"
        );

        String expectedIdentity = "selfLink";
        String actualIdentity = identifiable.identify();

        Assertions.assertEquals(expectedIdentity, actualIdentity);
    }

    @Test
    public void test_identify_leaderboard_perGame() {
        Identifiable identifiable = new Leaderboard(
                "webLink",

                "categorylink/category",
                null,
                "gamelink/game",

                "timing",
                0,
                new ArrayList<>(),
                new ArrayList<>()
        );

        String expectedIdentity = "https://www.speedrun.com/api/v1/leaderboards/game/category/category";
        String actualIdentity = identifiable.identify();

        Assertions.assertEquals(expectedIdentity, actualIdentity);
    }
    @Test
    public void test_identify_leaderboard_perLevel() {
        Identifiable identifiable = new Leaderboard(
                "webLink",

                "categorylink/category",
                "levellink/level",
                "gamelink/game",

                "timing",
                0,
                new ArrayList<>(),
                new ArrayList<>()
        );

        String expectedIdentity = "https://www.speedrun.com/api/v1/leaderboards/game/level/level/category";
        String actualIdentity = identifiable.identify();

        Assertions.assertEquals(expectedIdentity, actualIdentity);
    }

    @Test
    public void test_identify_run() {
        Identifiable identifiable = new Run(
                "webLink",
                "selfLink",
                "id",

                new ArrayList<>(),
                "categorylink/b",
                "gamelink/c",

                -0,
                LocalDate.of(1, 1, 1),
                LocalDateTime.of(1,1,1,1,1),
                LocalTime.of(1,1)
        );

        String expectedIdentity = "selfLink";
        String actualIdentity = identifiable.identify();

        Assertions.assertEquals(expectedIdentity, actualIdentity);
    }


}
