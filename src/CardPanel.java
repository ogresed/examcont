import coordinates.Coordinates;
import gui.view.StatusBar;
import utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class CardPanel extends JPanel {
    int monitorIndex;
    ManyMouseObserver mouseObserver;
    GeneralLogic general;
    TreeMap<Integer, Integer> winDevMap;

    StatusBar downBar;
    StatusBar upBar;

    static BufferedImage sun;
    static {
        try {
            sun = ImageIO.read(new File(System.getProperty("user.dir") +
                    Constants.RESOURCES_PATH + "\\pictures\\sun.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CardPanel(int index, GeneralLogic generalLogic,
                     StatusBar downBar) {
        this.monitorIndex = index;
        this.mouseObserver = generalLogic.mouseObserver;
        this.general = generalLogic;
        this.downBar = downBar;
        this.winDevMap = generalLogic.windowDevise;

        upBar = new StatusBar(200,100);
    }
    //todo: при нажатии на крестик и другий кнопки фрейма приложение не должно закрываться
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(sun, 0, 0, null);
        try {
            int deviceIndex = winDevMap.get(monitorIndex);
            Coordinates coordinates = mouseObserver.getCoordinates(deviceIndex);
            g.drawImage(PlayersFrame.cursor, coordinates.getX(), coordinates.getY(), null);
            upBar.setText(String.format("%d %d", coordinates.getX(), coordinates.getY()));
        } catch (IndexOutOfBoundsException ignored) {}
        repaint();
    }
}
