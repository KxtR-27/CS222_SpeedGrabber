package speedgrabber;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import speedgrabber.records.Game;
import speedgrabber.records.Guest;
import speedgrabber.records.User;
import speedgrabber.records.interfaces.Identifiable;
import speedgrabber.records.interfaces.Player;

import java.util.ArrayList;

public class ApiDataGrabberTest {

    @Test
    public void test_test_isCached() {
        ApiDataGrabber.test_addToCache(new Game(
                "weblink",
                "selflink",
                "id",
                "abbreviation",
                "name",

                new ArrayList<>(),
                new ArrayList<>()
        ));

        Assertions.assertTrue(ApiDataGrabber.test_isCached("id+abbreviation"));
        Assertions.assertFalse(ApiDataGrabber.test_isCached("otherid+otherabbreviation"));
    }

    @Test
    public void test_test_getCachedIdentifiable() {
        Identifiable expectedIdentifiable = new Game(
                "weblink",
                "selflink",
                "id",
                "abbreviation",
                "name",

                new ArrayList<>(),
                new ArrayList<>()
        );
        ApiDataGrabber.test_addToCache(expectedIdentifiable);
        Identifiable actualIdentifiable = ApiDataGrabber.test_getCachedIdentifiable("id");

        Assertions.assertEquals(expectedIdentifiable, actualIdentifiable);

        Assertions.assertNull(ApiDataGrabber.test_getCachedIdentifiable("notAValidIdentity"));
    }

    @Test
    public void test_getCachedPlayer() {
        User user = new User(null, "userIdentity", null, null, null, null, null);
        ApiDataGrabber.test_addToCache(user);
        Assertions.assertTrue(ApiDataGrabber.test_isCached(user.identify()));

        Guest guest = new Guest("guestIdentity", null, null);
        ApiDataGrabber.test_addToCache(guest);
        Assertions.assertTrue(ApiDataGrabber.test_isCached(guest.identify()));

        Player cachedUser = (Player) ApiDataGrabber.test_getCachedIdentifiable(((Player) user).identify());
        Player cachedGuest = (Player) ApiDataGrabber.test_getCachedIdentifiable(((Player) guest).identify());

        Assertions.assertEquals(user, cachedUser);
        Assertions.assertEquals(guest, cachedGuest);
    }
}
