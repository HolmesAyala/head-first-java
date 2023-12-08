package startup_game_test;

import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

import org.junit.Assert;

import startup_game.StartupGameUtils;
import startup_game.Point;

public class StartupGameUtilsTest {
    @Test
    public void test() {
        StartupGameUtils startupGameUtils = new StartupGameUtils();
        List<Point> unavailablePoints = new ArrayList<>(Arrays.asList());
        Random random = new Random(1);

        List<Point> pointsGenerated = startupGameUtils.generateStartUpPoints(
                10,
                10,
                3,
                1,
                unavailablePoints,
                random
        );

        System.out.println(Arrays.toString(pointsGenerated.toArray()));
    }

    @Test
    public void shouldGenerateRandomConsecutiveStartupPoints() {
        StartupGameUtils startupGameUtils = new StartupGameUtils();
        Random random = new Random(2);

        List<Point> randomConsecutivePoints = startupGameUtils.generateRandomConsecutiveStartupPoints(
                10,
                10,
                3,
                random
        );

        List<Point> expectedPoints = new ArrayList<>(Arrays.asList(
                new Point(8, 2),
                new Point(8, 1),
                new Point(8, 0)
        ));

        Assert.assertArrayEquals(expectedPoints.toArray(), randomConsecutivePoints.toArray());
    }

    @Test
    public void shouldGenerateHorizontalInitialAndFinalPoints() {
        Random random = new Random(1);

        StartupGameUtils startupGameUtils = new StartupGameUtils();

        int[][] randomInitialAndFinalPoints = startupGameUtils.generateRandomInitialAndFinalPoints(
                10,
                10,
                4,
                random
        );

        int[][] expectedInitialAndFinalPoints = {
                {5, 8},
                {2, 8},
        };

        Assert.assertArrayEquals(expectedInitialAndFinalPoints[0], randomInitialAndFinalPoints[0]);
        Assert.assertArrayEquals(expectedInitialAndFinalPoints[1], randomInitialAndFinalPoints[1]);
    }

    @Test
    public void shouldGenerateVerticalInitialAndFinalPoints() {
        Random random = new Random(2);

        StartupGameUtils startupGameUtils = new StartupGameUtils();

        int[][] randomInitialAndFinalPoints = startupGameUtils.generateRandomInitialAndFinalPoints(
                10,
                10,
                4,
                random
        );

        int[][] expectedInitialAndFinalPoints = {
                {8, 2},
                {8, 5},
        };

        Assert.assertArrayEquals(expectedInitialAndFinalPoints[0], randomInitialAndFinalPoints[0]);
        Assert.assertArrayEquals(expectedInitialAndFinalPoints[1], randomInitialAndFinalPoints[1]);
    }

    @Test
    public void shouldGeneratePointsBetweenVerticalPoints() {
        StartupGameUtils startupGameUtils = new StartupGameUtils();

        int[][] pointsBetweenResult = startupGameUtils.generatePointsBetween(
                -2,
                -2,
                -2,
                2
        );

        int[][] pointsBetweenExpectedResult = {
                {-2, -2},
                {-2, -1},
                {-2, 0},
                {-2, 1},
                {-2, 2},
        };

        Assert.assertArrayEquals(pointsBetweenResult, pointsBetweenExpectedResult);
    }

    @Test
    public void shouldGeneratePointsBetweenHorizontalPoints() {
        StartupGameUtils startupGameUtils = new StartupGameUtils();

        int[][] pointsBetweenResult = startupGameUtils.generatePointsBetween(
                -2,
                10,
                2,
                10
        );

        int[][] pointsBetweenExpectedResult = {
                {-2, 10},
                {-1, 10},
                {0, 10},
                {1, 10},
                {2, 10},
        };

        Assert.assertArrayEquals(pointsBetweenResult, pointsBetweenExpectedResult);
    }
}
