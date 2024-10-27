package speedgrabber.records;

import org.ocpsoft.prettytime.PrettyTime;

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
    public String identify() {return selflink;}

    @Override
    public String toString() {
        return String.format(
//              "## ........ ........... ...->"
                "%-3s %-25s %-25s %s",
                place,
                playerlinks().getFirst().split("/")[playerlinks.getFirst().split("/").length - 1],
                primaryTime.toString(),
                new PrettyTime().format(dateOfRun)
        );
    }
}
