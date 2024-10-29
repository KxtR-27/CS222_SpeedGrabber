package speedgrabber.records;

import speedgrabber.records.interfaces.Identifiable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record Run(
        String weblink,
        String selflink,
        String id,

        List<String> playerlinks,
        String categorylink,
        String gamelink,

        int place,
        LocalDate dateOfRun,
        LocalDateTime dateOfSubmission,
        LocalTime primaryTime

) implements Identifiable {
    @Override
    public String identify() {
        return selflink;
    }
}
