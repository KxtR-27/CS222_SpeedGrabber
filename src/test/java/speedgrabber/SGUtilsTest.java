package speedgrabber;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SGUtilsTest {
    @Test
    public void test_asLocalDateTime() {
        LocalDateTime expectedLDT = LocalDateTime.of(2024, 1, 21, 12, 20, 16);
        LocalDateTime actualLDT = SGUtils.asLocalDateTime("2024-01-21T12:20:16Z");

        Assertions.assertEquals(expectedLDT, actualLDT);
    }
    @Test
    public void test_asLocalDate() {
        LocalDate expectedDate = LocalDate.of(2024, 1, 21);
        LocalDate actualDate = SGUtils.asLocalDate("2024-01-21");

        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void test_asLocalTime_StringHours() {
        LocalTime expectedTime = LocalTime.of(2, 51, 34);
        LocalTime actualTime = SGUtils.asLocalTime("PT2H51M34S");

        Assertions.assertEquals(expectedTime, actualTime);
    }
    @Test
    public void test_asLocalTime_StringNoHours() {
        LocalTime expectedTime = LocalTime.of(0, 51, 34);
        LocalTime actualTime = SGUtils.asLocalTime("PT51M34S");

        Assertions.assertEquals(expectedTime, actualTime);
    }
    @Test
    public void test_asLocalTime_StringNoMinutes() {
        LocalTime expectedTime = LocalTime.of(2, 0, 34);
        LocalTime actualTime = SGUtils.asLocalTime("PT2H34S");

        Assertions.assertEquals(expectedTime, actualTime);
    }
    @Test
    public void test_asLocalTime_StringWithMilliseconds() {
        // The most precise time measurement on a speedrun is milliseconds.
        LocalTime expectedTime = LocalTime.of(2, 51, 34, 999 * 1000000);
        LocalTime actualTime = SGUtils.asLocalTime("PT2H51M34.999S");

        Assertions.assertEquals(expectedTime, actualTime);
    }


    @Test
    public void test_asLocalTime_WholeSeconds() {
        LocalTime expectedTime = LocalTime.of(2, 51, 34);
        LocalTime actualTime = SGUtils.asLocalTime(10294);

        Assertions.assertEquals(expectedTime, actualTime);
    }
    @Test
    public void test_asLocalTime_DecimalSeconds() {
        // A double would return n.99899999...,
        // so a float as n.999 is more accurate.
        LocalTime expectedTime = LocalTime.of(2, 51, 34, 999 * 1000000);
        LocalTime actualTime = SGUtils.asLocalTime(10294.999F);

        Assertions.assertEquals(expectedTime, actualTime);
    }
    @Test
    public void test_asLocalTime_NoHours() {
        // A double would return n.99899999...,
        // so a float as n.999 is more accurate.
        LocalTime expectedTime = LocalTime.of(0, 51, 34, 999 * 1000000);
        LocalTime actualTime = SGUtils.asLocalTime(3094.999F);

        Assertions.assertEquals(expectedTime, actualTime);
    }
    @Test
    public void test_asLocalTime_NoMinutes() {
        // A double would return n.99899999...,
        // so a float as n.999 is more accurate.
        LocalTime expectedTime = LocalTime.of(2, 0, 34, 999 * 1000000);
        LocalTime actualTime = SGUtils.asLocalTime(7234.999F);

        Assertions.assertEquals(expectedTime, actualTime);
    }
}
