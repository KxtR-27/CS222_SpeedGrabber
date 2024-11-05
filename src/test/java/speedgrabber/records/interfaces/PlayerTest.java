package speedgrabber.records.interfaces;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import speedgrabber.records.Guest;
import speedgrabber.records.User;

public class PlayerTest {
    @Test
    public void test_identify() {
        Player myPlayer = new Player() {
            final String playername = "name";
            final String playerlink = "link";

            @Override
            public String playername() {
                return playername;
            }

            @Override
            public String playerlink() {
                return playerlink;
            }
        };
        Assertions.assertEquals("link", myPlayer.identify());
    }

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
