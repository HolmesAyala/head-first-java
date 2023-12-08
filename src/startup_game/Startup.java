package startup_game;

import java.util.List;
import java.util.ArrayList;

public class Startup {
    private List<Point> points;
    private List<Point> damagedPoints;

    Startup() {
        this.setPoints(new ArrayList());
        this.setDamagedPoints(new ArrayList());
    }

    /**
     * @return If the damaged point is a startup point,
     * and it is a new damaged point it returns True,
     * otherwise False
     */
    public boolean addDamagedPoint(Point newDamagedPoint) {
        boolean isAStartupPoint = this.points.stream().anyMatch(startUpPoint -> startUpPoint.equals(newDamagedPoint));
        boolean isADamagedPoint = this.damagedPoints.stream().anyMatch(damagedPoint -> damagedPoint.equals(newDamagedPoint));

        if(isAStartupPoint && !isADamagedPoint) {
            this.damagedPoints.add(newDamagedPoint);

            return true;
        }

        return false;
    }

    /**
     * X and Y positions
     */
    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Point> getDamagedPoints() {
        return damagedPoints;
    }

    public void setDamagedPoints(List<Point> damagedPoints) {
        this.damagedPoints = damagedPoints;
    }
}
