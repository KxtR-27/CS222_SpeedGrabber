package speedgrabber;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import speedgrabber.records.Game;
import speedgrabber.records.Identifiable;

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
}
