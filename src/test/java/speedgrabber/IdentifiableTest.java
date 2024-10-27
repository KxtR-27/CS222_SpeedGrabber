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
                "weblink",
                "selflink",
                "id",
                "abbreviation",
                "name",

                new ArrayList<>()
        );

        String expectedIdentity = "id+abbreviation";
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
    public void test_identify_leaderboard() {
        Identifiable identifiable = new Leaderboard(
                "weblink",

                "categorylink/category",
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


}
