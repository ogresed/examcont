public class Coordinates {
    private int x, y;
    private int Xmax, Ymax;

    public Coordinates() {
        Xmax = 1000;
        Ymax = 1000;
    }

    public int getXmax() {
        return Xmax;
    }

    public void setXmax(int xmax) {
        Xmax = xmax;
    }

    public int getYmax() {
        return Ymax;
    }

    public void setYmax(int ymax) {
        Ymax = ymax;
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
