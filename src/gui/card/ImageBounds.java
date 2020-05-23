package gui.card;

import java.awt.image.BufferedImage;

public class ImageBounds {
    public int x, y;
    public final int width, height;
    BufferedImage image;

    public ImageBounds(final BufferedImage image) {
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
