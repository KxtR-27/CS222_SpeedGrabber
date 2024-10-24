package speedgrabber;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SGUtils {
    public static LocalDateTime asLocalDateTime(String iso8601) {
        if (iso8601 != null)
            return LocalDateTime.parse(iso8601.substring(0, 19));

        return null;
    }
    public static LocalDate asLocalDate(String iso8601) {
        if (iso8601 != null)
            return LocalDate.parse(iso8601);

        return null;
    }
}
