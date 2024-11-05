package speedgrabber;

import speedgrabber.apidatagrabbers.CategoryGrabber;
import speedgrabber.apidatagrabbers.GameGrabber;
import speedgrabber.apidatagrabbers.LeaderboardGrabber;
import speedgrabber.apidatagrabbers.RunGrabber;
import speedgrabber.records.Category;
import speedgrabber.records.Game;
import speedgrabber.records.Leaderboard;
import speedgrabber.records.Run;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

public class MainCLI {
    public static final Scanner consoleScanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.printf("%nPlease enter a game abbreviation or ID. (Ex. 'sms' or 'v1pxjz68')%n>> ");
        String gameNameInput = consoleScanner.nextLine();

        try {
            System.out.printf("%nSearching... ");
            Game game = GameGrabber.grab(gameNameInput);
            System.out.printf("Game Found! [%s]%n%n", game.name());

            List<Category> categoryList = CategoryGrabber.grabList(game);
            System.out.printf("Enter # for desired Category.%n%s%n", "-".repeat(20));
            for (int i = 1; i <= categoryList.size(); i++)
                System.out.printf("* %-3s %s%n", String.format("%d.", i), categoryList.get(i-1));
            System.out.println("-".repeat(20));

            System.out.print(">> ");
            int categoryIndex = Integer.parseInt(consoleScanner.nextLine()) - 1;

            Leaderboard leaderboard = LeaderboardGrabber.grab(categoryList.get(categoryIndex).leaderboardlink(), 20);
            List<Run> leaderboardRuns = RunGrabber.grabList(leaderboard, 20);

            System.out.printf("%nLeaderboard for: %s%n%s%n", categoryList.get(categoryIndex).name(), "-".repeat(20));
            for (Run run : leaderboardRuns)
                System.out.printf("#%-3s %s%n", String.format("%d.", run.place()), run);
            System.out.println("-".repeat(20));
        }
        catch (FileNotFoundException error404) {
            throw new FileNotFoundException(String.format(
                    "Whoops! There is no game with ID \"%s\".%nYou can try searching online at %s%s",
                    gameNameInput, "https://www.speedrun.com/search?q=", gameNameInput
            ));
        }
        catch (UnknownHostException internetError) {
            throw new UnknownHostException("A network error occurred. Please check your internet connection");
        }

        consoleScanner.close();
        System.exit(0);
    }
}
