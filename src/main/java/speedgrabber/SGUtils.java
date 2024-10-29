package speedgrabber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    public static LocalTime asLocalTime(String primaryTime) {
        int hours = 0; int minutes = 0; float seconds = 0; int milliseconds;
        int indexH, indexM, indexS;

        // Remove "PT"
        primaryTime = primaryTime.substring(2);

        indexH = primaryTime.indexOf('H');
        if (indexH != -1) {
            hours = Integer.parseInt(primaryTime.substring(0, indexH));
            primaryTime = primaryTime.substring(indexH + 1);
        }

        indexM = primaryTime.indexOf('M');
        if (indexM != -1) {
            minutes = Integer.parseInt(primaryTime.substring(0, indexM));
            primaryTime = primaryTime.substring(indexM + 1);
        }

        indexS = primaryTime.indexOf('S');
        if (indexS != -1)
            seconds = Float.parseFloat(primaryTime.substring(0, indexS));

        milliseconds = (int) (seconds % 1 * 1000);
        seconds = (int) seconds;

        return LocalTime.of(hours, minutes, (int) seconds, milliseconds * 1000000);
    }
    public static LocalTime asLocalTime(float seconds) {
        int hours, minutes;
        int milliseconds;

        hours = (int) (seconds / 3600);
        seconds -= hours * 3600;

        minutes = (int) (seconds / 60);
        seconds -= minutes * 60;

        milliseconds = (int) (seconds % 1 * 1000);

        return LocalTime.of(hours, minutes, (int) seconds, milliseconds * 1000000);
    }


    public static String grabEndOfSplit(String toSplit, String splitter) {
        return toSplit.split(splitter)[toSplit.split(splitter).length - 1];
    }

    public static String encodeSlug(String slug) {
        return slug.replace(' ', '+');
    }
}
