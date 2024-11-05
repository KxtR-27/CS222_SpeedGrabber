package speedgrabber.jsonreaders;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import speedgrabber.records.Guest;
import speedgrabber.records.User;
import speedgrabber.records.interfaces.Player;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class PlayerReaderTest {
    @BeforeAll
    public static void initializeJsonFiles() throws IOException {
        userJson =
                IOUtils.toString(Objects.requireNonNull(PlayerReaderTest.class.getResource("example-user.json")), StandardCharsets.UTF_8);
        guestJson =
                IOUtils.toString(Objects.requireNonNull(PlayerReaderTest.class.getResource("example-guest.json")), StandardCharsets.UTF_8);
    }
    static String userJson, guestJson;

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
        User actualuser = (User) PlayerReader.create(userJson);

        Assertions.assertEquals(expectedUser, actualuser);
    }
    @Test
    public void test_createGuest() {
        Guest expectedGuest = new Guest(
                "https://www.speedrun.com/api/v1/guests/Alex",
                "Alex",

                "https://www.speedrun.com/api/v1/runs?guest=Alex"
        );
        Guest actualGuest = (Guest) PlayerReader.create(guestJson);

        Assertions.assertEquals(expectedGuest, actualGuest);
    }
    @Test
    public void test_createPlayer() {
        Player playerGuest = PlayerReader.create(guestJson);
        Assertions.assertInstanceOf(Guest.class, playerGuest);

        Player playerUser = PlayerReader.create(userJson);
        Assertions.assertInstanceOf(User.class, playerUser);
    }
}
