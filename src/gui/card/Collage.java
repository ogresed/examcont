package gui.card;

import java.awt.image.BufferedImage;

public class Collage {
    BufferedImage collage;
    public Card[] cardsInCollage;

    public Collage () {
        cardsInCollage = new Card[20];
    }

    public BufferedImage getCollage() {
        return collage;
    }

    public void setCollage(BufferedImage collage) {
        this.collage = collage;
    }
}
