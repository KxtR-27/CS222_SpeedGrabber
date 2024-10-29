package speedgrabber;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import speedgrabber.records.*;
import speedgrabber.records.interfaces.Identifiable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class IdentifiableTest {
    @Test
    public void test_identify_game() {
        Identifiable identifiable = new Game(
                "weblink",
                "selflink",
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
                "weblink",
                "selflink",
                "id",
                "name",

                "gamelink"
        );

        String expectedIdentity = "selflink";
        String actualIdentity = identifiable.identify();

        Assertions.assertEquals(expectedIdentity, actualIdentity);
    }

    @Test
    public void test_identify_category() {
        Identifiable identifiable = new Category(
                "weblink",
                "selflink",
                "id",
                "name",

                "leaderboard",
                "game",

                "type"
        );

        String expectedIdentity = "selflink";
        String actualIdentity = identifiable.identify();

        Assertions.assertEquals(expectedIdentity, actualIdentity);
    }

    @Test
    public void test_identify_leaderboard_perGame() {
        Identifiable identifiable = new Leaderboard(
                "weblink",

                "categorylink/category",
                null,
                "gamelink/game",

                "timing",
                0,
                new ArrayList<>(),
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
                "weblink",

                "categorylink/category",
                "levellink/level",
                "gamelink/game",

                "timing",
                0,
                new ArrayList<>(),
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
                "weblink",
                "selflink",
                "id",

                new ArrayList<>(),
                "categorylink/b",
                "gamelink/c",

                -0,
                LocalDate.of(1, 1, 1),
                LocalDateTime.of(1,1,1,1,1),
                LocalTime.of(1,1)
        );

        String expectedIdentity = "selflink";
        String actualIdentity = identifiable.identify();

        Assertions.assertEquals(expectedIdentity, actualIdentity);
    }

    @Test
    public void test_identify_user() {
        Identifiable identifiable = new User(
                null, "selflink", null, null, null, null, null
        );

        String expectedIdentity = "selflink";
        String actualIdentity = identifiable.identify();

        Assertions.assertEquals(expectedIdentity, actualIdentity);
    }
    @Test
    public void test_identify_guest() {
        Identifiable identifiable = new Guest(
                "selflink", null, null
        );

        String expectedIdentity = "selflink";
        String actualIdentity = identifiable.identify();

        Assertions.assertEquals(expectedIdentity, actualIdentity);
    }
}
