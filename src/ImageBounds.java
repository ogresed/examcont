import java.awt.image.BufferedImage;

public class ImageBounds {
    int x, y;
    final int width, height;
    BufferedImage image;

    ImageBounds(final BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();
        this.image = image;
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
