package speedgrabber;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
