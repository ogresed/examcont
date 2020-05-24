import coordinates.Coordinates;
import gui.card.Card;
import gui.card.Collage;
import gui.card.CollageBuilder;
import gui.view.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.TreeMap;

public class CardPanel extends JPanel {
    int monitorIndex;
    ManyMouseObserver mouseObserver;
    GeneralLogic general;
    TreeMap<Integer, Integer> winDevMap;

    StatusBar downBar;
    StatusBar upBar;

    private volatile Image showedImage = null;

    public CardPanel(int index, GeneralLogic generalLogic,
                     StatusBar downBar) {
        this.monitorIndex = index;
        this.mouseObserver = generalLogic.mouseObserver;
        this.general = generalLogic;
        this.downBar = downBar;
        this.winDevMap = generalLogic.windowDevise;

        upBar = new StatusBar(200,100);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            g.drawImage(showedImage, 0, 0, null);
        } catch (NullPointerException ignored) {
            System.out.println(123123123);
        }
        try {
            int deviceIndex = winDevMap.get(monitorIndex);
            Coordinates coordinates = mouseObserver.getCoordinates(deviceIndex);
            g.drawImage(PlayersFrame.cursor, coordinates.getX(), coordinates.getY(), null);
            upBar.setText(String.format("%d %d", coordinates.getX(), coordinates.getY()));
        } catch (IndexOutOfBoundsException ignored) {}
        repaint();
    }

    public void setPicture(BufferedImage collage) {
        showedImage = collage;
    }

    public void setChooseAction() {
        mouseObserver.setAction(monitorIndex, this::onCardChooseAction);
    }

    private void onCardChooseAction(int xC, int yC) {
        int x = xC / Card.width;
        int y = yC / Card.height;
        int cardIndex = x + y * CollageBuilder.collageWidth;
        if(cardIndex > CollageBuilder.collageSize - 1 || x > CollageBuilder.collageWidth) {
            return;
        }
        System.out.println(general.collage.cardsInCollage[cardIndex].name);
        actionWithCard(monitorIndex, general.collage.cardsInCollage[cardIndex]);
    }

    private void actionWithCard(int monitorIndex, Card card) {
        
    }
}
