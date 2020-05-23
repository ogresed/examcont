package gui.card;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Card {
    static int thickness = 4;
    static int width = 200;
    static int height = 150;

    int xBound;
    int yBound;

    int index;

    String prefix;
    String name;
    String description;
    BufferedImage picture;

    public Card(String prefix, String name, String description, BufferedImage picture) {
        this.prefix = prefix;
        this.name = name;
        this.description = description;
        this.picture = buildPicture(picture);
    }

    public BufferedImage buildPicture(BufferedImage picture) {
        int widthForPic = width - 2 * thickness;
        int heightForPic = height - 2 * thickness - height/5;
        BufferedImage buildImage = new BufferedImage(width,
                height, picture.getType());

        Graphics2D g2d = buildImage.createGraphics();
        
        for(int i = 0; i < buildImage.getHeight(); i++) {
            for(int j = 0; j < buildImage.getWidth(); j++) {
                buildImage.setRGB(j, i, Color.WHITE.getRGB());
            }
        }
        g2d.setColor(Color.BLACK);
        g2d.drawImage(picture, thickness, thickness, widthForPic, heightForPic, null);
        int heightForName = 4*height/5 + height/10;
        g2d.drawString("    " + name, 0, heightForName);
        g2d.dispose();
        return buildImage;
    }
}