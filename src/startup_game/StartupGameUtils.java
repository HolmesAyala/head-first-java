package startup_game;

import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StartupGameUtils {
    public List<Startup> generateStartups(
            int startupsAmount,
            int boardRows,
            int boardColumns,
            int startupSize
    ) {
        return this.generateStartups(startupsAmount, boardRows, boardColumns, startupSize, new Random());
    }

    public List<Startup> generateStartups(
            int startupsAmount,
            int boardRows,
            int boardColumns,
            int startupSize,
            Random random
    ) {
        List<Startup> startups = new ArrayList<>();

        for (int i = 1; i <= startupsAmount; i++) {
            Startup newStartup = new Startup();

            List<Point> points = this.generateStartUpPoints(
                    boardRows,
                    boardColumns,
                    startupSize,
                    50,
                    startups.stream().flatMap(startup -> startup.getPoints().stream()).collect(Collectors.toList()),
                    random
            );

            if (points == null) {
                throw new RuntimeException(String.format("Could not generate the positions for startup %d of %d", i, startupsAmount));
            }

            newStartup.setPoints(points);

            startups.add(newStartup);
        }

        return startups;
    }

    public List<Point> generateStartUpPoints(
            int boardRows,
            int boardColumns,
            int startupCells,
            int attempts,
            List<Point> unavailablePositions,
            Random random
    ) {
        while (attempts > 0) {
            List<Point> points = this.generateRandomConsecutiveStartupPoints(boardRows, boardColumns, startupCells, random);

            boolean isEqualToUnavailablePosition = points.stream()
                    .anyMatch(startUpPoint -> unavailablePositions.stream().anyMatch(
                            unavailablePoint -> (startUpPoint.getX() == unavailablePoint.getX()) && startUpPoint.getY() == unavailablePoint.getY()
                    ));

            if (!isEqualToUnavailablePosition) {
                return points;
            }

            attempts--;
        }

        return null;
    }

    /**
     * @param boardColumns - From 1 to N
     * @param boardRows    - From 1 to N
     * @param points       - Consecutive positions to generate
     */
    public List<Point> generateRandomConsecutiveStartupPoints(
            int boardRows,
            int boardColumns,
            int points,
            Random random) {
        int[][] initialAndFinalPoints = this.generateRandomInitialAndFinalPoints(boardRows, boardColumns, points, random);

        int initialX = initialAndFinalPoints[0][0];
        int initialY = initialAndFinalPoints[0][1];
        int finalX = initialAndFinalPoints[1][0];
        int finalY = initialAndFinalPoints[1][1];

        int[][] pointsBetweenInitialAndFinal = this.generatePointsBetween(initialX, initialY, finalX, finalY);

        return Arrays.stream(pointsBetweenInitialAndFinal)
                .map(point -> new Point(point[0], point[1]))
                .collect(Collectors.toList());
    }

    /**
     * @return {{initialX, initialY}, {finalX, finalY}}
     */
    public int[][] generateRandomInitialAndFinalPoints(int maxY, int maxX, int distance, Random random) {
        if (distance < 1) throw new RuntimeException("The distance should be greater or equal to 1");

        if (maxX < 1 || maxY < 1)
            throw new RuntimeException("The maxX or maxY should be greater or equal to 1");

        if (maxX < distance && maxY < distance)
            throw new RuntimeException("The maxX or maxY should be greater than the distance");

        int initialX = random.nextInt(maxX);
        int initialY = random.nextInt(maxY);
        int distanceFromFirstPoint = distance - 1;

        // 1: up, 2: right, 3: down, 4: left
        List<Integer> directions = Arrays.asList(1, 2, 3, 4);

        Collections.shuffle(directions, random);

        for (int direction : directions) {
            int finalX = initialX + (direction == 2 ? distanceFromFirstPoint : direction == 4 ? -distanceFromFirstPoint : 0);
            int finalY = initialY + (direction == 1 ? distanceFromFirstPoint : direction == 3 ? -distanceFromFirstPoint : 0);

            if (finalX >= 0 && finalX < maxX && finalY >= 0 && finalY < maxY) {

                return new int[][]{
                        {initialX, initialY},
                        {finalX, finalY}
                };
            }
        }

        throw new RuntimeException(
                String.format(
                        "Unable to generate initial and final points: maxY=%d, maxX=%d, distance=%d, initialX=%d, initialY=%d",
                        maxY,
                        maxX,
                        distance,
                        initialX,
                        initialY
                )
        );
    }

    /**
     * The initial and end points are included
     */
    public int[][] generatePointsBetween(int fromX, int fromY, int toX, int toY) {
        int pointsToGenerate = Math.max(Math.abs(toX - fromX), Math.abs(toY - fromY)) + 1;

        int[][] points = new int[pointsToGenerate][2];

        int xDelta = Integer.compare(toX, fromX);
        int yDelta = Integer.compare(toY, fromY);

        int generatedX = fromX;
        int generatedY = fromY;

        for (int i = 0; i < pointsToGenerate; i++) {
            points[i] = new int[]{generatedX, generatedY};

            generatedX += xDelta;
            generatedY += yDelta;
        }

        return points;
    }
}
