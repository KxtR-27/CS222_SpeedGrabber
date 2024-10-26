package speedgrabber.records;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record Run(
        String webLink,
        String selfLink,
        String id,

        List<String> linksToPlayers,
        String linkToCategory,
        String linkToGame,

        int place,
        LocalDate dateOfRun,
        LocalDateTime dateOfSubmission,
        LocalTime primaryTime
) {}
