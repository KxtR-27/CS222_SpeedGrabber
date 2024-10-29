package speedgrabber;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import speedgrabber.records.Guest;
import speedgrabber.records.User;
import speedgrabber.records.interfaces.Player;

public class PlayerTest {
    @Test
    public void test_playername_guest() {
        Player player = new Guest(
                "selflink",
                "guest name",

                "runslink"
        );

        String expectedPlayername = "guest name*";
        String actualPlayername = player.playername();

        Assertions.assertEquals(expectedPlayername, actualPlayername);
    }
    @Test
    public void test_playername_user() {
        Player player = new User(
                "weblink",
                "selflink",
                "id",
                "username",

                "user",
                "runslink",
                "pbslink"
        );

        String expectedPlayername = "username";
        String actualUsername = player.playername();

        Assertions.assertEquals(expectedPlayername, actualUsername);
    }
    @Test
    public void test_playername_nonpolymorphed() {
        User user = new User(null, null, null, "username", null, null, null);
        Guest guest = new Guest(null, "guestname", null);

        String expectedUserPlayername = "username";
        String actualUserPlayername = user.playername();

        String expectedGuestPlayername = "guestname*";
        String actualGuestPlayername = guest.playername();

        Assertions.assertEquals(expectedUserPlayername, actualUserPlayername);
        Assertions.assertEquals(expectedGuestPlayername, actualGuestPlayername);
    }
}
