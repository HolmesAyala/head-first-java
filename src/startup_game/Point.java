package startup_game;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", this.getX(), this.getY());
    }

    @Override
    public boolean equals(Object object) {
        if(object == null) return false;

        if(!(object instanceof Point)) return false;

        if(this == object) return true;

        Point pointToCompare = (Point) object;

        if(pointToCompare.getX() != this.getX() || pointToCompare.getY() != this.getY()) return false;

        return true;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
