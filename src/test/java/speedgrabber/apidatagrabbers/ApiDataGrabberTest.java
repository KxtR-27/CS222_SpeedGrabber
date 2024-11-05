package speedgrabber.apidatagrabbers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import speedgrabber.records.Game;
import speedgrabber.records.Guest;
import speedgrabber.records.User;
import speedgrabber.records.interfaces.Identifiable;
import speedgrabber.records.interfaces.Player;

import java.util.ArrayList;
import java.util.Objects;

public class ApiDataGrabberTest {

    @Test
    public void test_isCached() {
        ApiDataGrabber.addToCache(new Game(
                "weblink",
                "selflink",
                "id",
                "abbreviation",
                "name",

                new ArrayList<>(),
                new ArrayList<>()
        ));

        Assertions.assertTrue(ApiDataGrabber.isCached("id+abbreviation"));
        Assertions.assertFalse(ApiDataGrabber.isCached("otherid+otherabbreviation"));
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
        ApiDataGrabber.addToCache(expectedIdentifiable);
        Identifiable actualIdentifiable = ApiDataGrabber.getCachedIdentifiable("id");

        Assertions.assertEquals(expectedIdentifiable, actualIdentifiable);

        Assertions.assertNull(ApiDataGrabber.getCachedIdentifiable("notAValidIdentity"));
    }

    @Test
    public void test_getCachedPlayer() {
        User user = new User(null, "userIdentity", null, null, null, null, null);
        ApiDataGrabber.addToCache(user);
        Assertions.assertTrue(ApiDataGrabber.isCached(user.identify()));

        Guest guest = new Guest("guestIdentity", null, null);
        ApiDataGrabber.addToCache(guest);
        Assertions.assertTrue(ApiDataGrabber.isCached(guest.identify()));

        Player cachedUser = (Player) ApiDataGrabber.getCachedIdentifiable(((Player) user).identify());
        Player cachedGuest = (Player) ApiDataGrabber.getCachedIdentifiable(((Player) guest).identify());

        Assertions.assertEquals(user, cachedUser);
        Assertions.assertEquals(guest, cachedGuest);
    }

    @Test
    public void test_replaceInCache() {
        Identifiable oldIdentifiable = new Guest("self", "oldName", "oldRuns");
        ApiDataGrabber.addToCache(oldIdentifiable);
        Assertions.assertTrue(ApiDataGrabber.isCached("self"));
        Assertions.assertEquals(
                "oldName",
                ((Guest) (Objects.requireNonNull(ApiDataGrabber.getCachedIdentifiable(oldIdentifiable.identify())))).name()
        );

        Identifiable newIdentifiable = new Guest("self", "newName", "newRuns");
        ApiDataGrabber.replaceInCache(newIdentifiable);
        Assertions.assertTrue(ApiDataGrabber.isCached("self"));
        Assertions.assertEquals(
                "newName",
                ((Guest) (Objects.requireNonNull(ApiDataGrabber.getCachedIdentifiable(oldIdentifiable.identify())))).name()
        );
    }
}
