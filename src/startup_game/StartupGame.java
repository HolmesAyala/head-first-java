package startup_game;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * - One row
 * - Read the user input
 * - Every start up fills 3 cells
 */

public class StartupGame {
    private final int BOARD_ROWS = 1;
    private final int BOARD_COLUMNS = 7;
    private final int DEFAULT_STARTUPS_AMOUNT = 1;
    private final int DEFAULT_STARTUP_SIZE = 3;
    private List<Startup> startups;
    private List<Point> attemptPoints;

    public void start() {
        StartupGameUtils startupGameUtils = new StartupGameUtils();

        this.startups = startupGameUtils.generateStartups(
                this.DEFAULT_STARTUPS_AMOUNT,
                this.BOARD_ROWS,
                this.BOARD_COLUMNS,
                this.DEFAULT_STARTUP_SIZE
        );

        this.attemptPoints = new ArrayList<>();

        boolean continueGame = true;

        while(continueGame) {
            this.printBoard(this.BOARD_ROWS, this.BOARD_COLUMNS, this.attemptPoints, this.startups);

            Point guessPoint = this.requestGuess(this.BOARD_ROWS, this.BOARD_COLUMNS);

            for(Startup startup : startups) {
                boolean startupPointAlreadyHit = startup.getDamagedPoints().stream().anyMatch(damagedPoint -> damagedPoint.equals(guessPoint));

                if(startupPointAlreadyHit) {
                    System.out.println("Point already hit, try again.\n");
                    break;
                }

                boolean hitStartup = startup.getPoints().stream().anyMatch(startupPoint -> startupPoint.equals(guessPoint));

                if(hitStartup) {
                    System.out.println("Startup damaged!\n");

                    startup.addDamagedPoint(guessPoint);

                    break;
                }

                System.out.println("Shot missed.\n");

                this.attemptPoints.add(guessPoint);
            }

            boolean allStartupsFullyDamaged = startups.stream().allMatch(startup -> startup.getPoints().size() == startup.getDamagedPoints().size());

            if(allStartupsFullyDamaged) {
                this.printBoard(this.BOARD_ROWS, this.BOARD_COLUMNS, this.attemptPoints, this.startups);

                System.out.println("All startups destroyed!");

                continueGame = false;
            }
        }
    }

    private void printBoard(int rows, int columns, List<Point> attemptPoints, List<Startup> startups) {
        System.out.println("=== BOARD ===");

        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                Point point = new Point(column, row);

                boolean pointInAttemptPoints = attemptPoints.stream().anyMatch(
                        attemptPoint -> attemptPoint.equals(point)
                );

                if(pointInAttemptPoints) {
                    System.out.print("[•]");
                    continue;
                }

                boolean pointIsAStartupDamagedPoint = startups.stream().anyMatch(
                        startup -> startup.getDamagedPoints().stream().anyMatch(damagedPoint -> damagedPoint.equals(point))
                );

                if(pointIsAStartupDamagedPoint) {
                    System.out.print("[×]");
                    continue;
                }

                System.out.print("[ ]");
            }

            System.out.print("\n");
        }
    }

    private Point requestGuess(int rows, int columns) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Guess the startups position.");
        System.out.print("Row (" + 1 + " - " + rows + "): ");

        int row = scanner.nextInt() - 1;

        System.out.print("Column (" + 1 + " - " + columns + "): ");

        int column = scanner.nextInt() - 1;

        return new Point(column, row);
    }
}
