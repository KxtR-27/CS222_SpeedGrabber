package speedgrabber;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import speedgrabber.records.Game;
import speedgrabber.records.Identifiable;

import java.util.ArrayList;

public class ApiDataGrabberTest {
    @Test
    public void test_whatsMyCoverage() {
        System.out.println(
                """
                To be so honest, I'm not even sure how to test this class.
                It's all web code. It is so simple that I cannot even test the code it has.
                I would happily be proven wrong, though."""
        );
    }

    @Test
    public void test_test_isCached() {
        ApiDataGrabber.test_addToCache(new Game(
                "weblink",
                "selflink",
                "id",
                "name",

                new ArrayList<>()
        ));

        Assertions.assertTrue(ApiDataGrabber.test_isCached("selflink"));
        Assertions.assertFalse(ApiDataGrabber.test_isCached("otherlink"));
    }

    @Test
    public void test_test_getCachedIdentifiable() {
        Identifiable expectedIdentifiable = new Game(
                "weblink",
                "selflink",
                "id",
                "name",

                new ArrayList<>()
        );
        ApiDataGrabber.test_addToCache(expectedIdentifiable);
        Identifiable actualIdentifiable = ApiDataGrabber.test_getCachedIdentifiable("selflink");

        Assertions.assertEquals(expectedIdentifiable, actualIdentifiable);

        Assertions.assertNull(ApiDataGrabber.test_getCachedIdentifiable("notAValidIdentity"));
    }
}
