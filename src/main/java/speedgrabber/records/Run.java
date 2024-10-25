package speedgrabber.records;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record Run(
        String webLink,
        String selfLink,
        String id,

        String gameLink,
        String categoryLink,
        List<String> playerLinks,

        int place,
        LocalDate dateOfRun,
        LocalDateTime dateOfSubmission,
        LocalTime primaryTime
) {
    public Run(String webLink, String selfLink, String id, String gameLink, String categoryLink, List<String> playerLinks, LocalDate dateOfRun, LocalDateTime dateOfSubmission, LocalTime primaryTime) {
        this(webLink, selfLink, id, gameLink, categoryLink, playerLinks, -1, dateOfRun, dateOfSubmission, primaryTime);
    }
}
