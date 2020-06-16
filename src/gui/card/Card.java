package gui.card;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Card {
    static int thickness = 4;
    public static int width = 200;
    public static int height = 150;

		int xBound;
		int yBound;

    int index;

    String prefix;
		public String name;
		public String description;
		public BufferedImage picture;

    public Card(String prefix, String name, String description, BufferedImage picture) {
        this.prefix = prefix;
        this.name = name;
        this.description = description;
        this.picture = buildPicture(picture);
    }

    public BufferedImage buildPicture(BufferedImage picture) {
        int startPictureWidth = picture.getWidth();
        int startPictureHeight = picture.getHeight();
        double sizeRelative = (startPictureWidth * 1.0) / startPictureHeight;

        int heightForPic = (4 * height) / 5 - thickness;
        int widthForPic =  (int)(heightForPic * sizeRelative);

        BufferedImage buildImage = new BufferedImage(width,
                height, picture.getType());

        Graphics2D g2d = buildImage.createGraphics();
        
        for(int i = 0; i < buildImage.getHeight(); i++) {
            for(int j = 0; j < buildImage.getWidth(); j++) {
                buildImage.setRGB(j, i, Color.WHITE.getRGB());
            }
        }
        g2d.setColor(Color.BLACK);
        Font currentFont = g2d.getFont();
        Font newFont = currentFont.deriveFont(height * 1.0F / 8);
        g2d.setFont(newFont);
        g2d.setFont(newFont);

        g2d.drawImage(picture, thickness, thickness, widthForPic, heightForPic, null);
        int heightForName = height - height / 30;
        g2d.drawString(name, width / 8, heightForName);
        g2d.dispose();
        return buildImage;
    }
}