package speedgrabber;

import speedgrabber.records.Category;
import speedgrabber.records.Game;
import speedgrabber.records.Leaderboard;
import speedgrabber.records.Run;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MainCLI {
    public static final Scanner consoleScanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.printf("%nPlease enter a game abbreviation or ID. (Ex. 'sms' or 'v1pxjz68')%n>> ");
        String gameNameInput = consoleScanner.nextLine();

        System.out.printf("%nSearching... ");
        Game game = ApiDataGrabber.getGame(gameNameInput);
        System.out.printf("Game Found! [%s]%n%n", game.name());

        List<Category> categoryList = ApiDataGrabber.getCategories(game);
        System.out.printf("Enter # for desired Category.%n%s%n", "-".repeat(20));
        for (int i = 1; i <= categoryList.size(); i++)
            System.out.printf("* %-3s %s%n", String.format("%d.", i), categoryList.get(i-1));
        System.out.println("-".repeat(20));

        System.out.print(">> ");
        int categoryIndex = Integer.parseInt(consoleScanner.nextLine()) - 1;

        Leaderboard leaderboard = ApiDataGrabber.getLeaderboard(categoryList.get(categoryIndex), 20);
        List<Run> leaderboardRuns = ApiDataGrabber.getListOfRuns(leaderboard);

        System.out.printf("%nLeaderboard for: %s%n%s%n", categoryList.get(categoryIndex).name(), "-".repeat(20));
        for (Run run : leaderboardRuns)
            System.out.printf("#%-3s %s%n", String.format("%d.", run.place()), run);
        System.out.println("-".repeat(20));

        consoleScanner.close();
        System.exit(0);
    }
}
