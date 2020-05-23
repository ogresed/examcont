import coordinates.Coordinates;
import gui.card.ImageBounds;
import gui.view.MonologueBar;
import util.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class IntroPanel extends JPanel {
    private static final String waitOtherMessage = "Ожидайте остальных";
    private static final String introduction = "Привет это вступление к игру  'Контакт'. На этом месте будут изложены правила игры.";

    ReadyChecker readyChecker;
    int monitorIndex;
    SwitchedPanel panel;
    ManyMouseObserver observer;
    Map<Integer, Integer> winDevMap;
    MonologueBar monologueBar;

    public static BufferedImage startImage;
    static {
        try {
            startImage = ImageIO.read(new File(System.getProperty("user.dir") +
                    Constants.RESOURCES_PATH + "\\pictures\\start.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    final ImageBounds startBounds;
    String message = introduction;

    public IntroPanel(ReadyChecker readyChecker, int index, SwitchedPanel panel,
                      ManyMouseObserver observer,
                      TreeMap<Integer, Integer> winDevMap, MonologueBar monologueBar) {
        this.readyChecker = readyChecker;
        this.monitorIndex = index;
        this.panel = panel;
        this.observer = observer;
        this.winDevMap = winDevMap;
        this.monologueBar = monologueBar;
        monologueBar.setText(introduction);

        setBackground(Color.WHITE);

        startBounds = new ImageBounds(startImage);
        startBounds.setX(200);
        startBounds.setY(100);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        setBoundsForStartButton();
        int x = startBounds.getX();
        int y = startBounds.getY();

        g.drawImage(startImage, x, y, null);

        try {
            int deviceIndex = winDevMap.get(monitorIndex);
            Coordinates coordinates = observer.getCoordinates(deviceIndex);

            g.drawImage(PlayersFrame.cursor, coordinates.getX(), coordinates.getY(), null);
        } catch (IndexOutOfBoundsException | NullPointerException ignored) {}
        repaint();
    }

    private void setBoundsForStartButton() {
        Rectangle r = getBounds();
        int y = r.height - startBounds.height - 1;
        int x = (r.width - startBounds.width) / 2;
        startBounds.setX(x);
        startBounds.setY(y);
    }
    // синхронизация кнопок старт работает не до конца
    public void OnStart(int x, int y) {
        int XPos = startBounds.x;
        int YPos = startBounds.y;
        int width = startBounds.width;
        int height = startBounds.height;
        if(x >= XPos && x <= width + XPos &&
            y >= YPos && y <= height + YPos) {
            monologueBar.setText(waitOtherMessage);
            readyChecker.ready(panel, monitorIndex);
        }
    }

    public void setAction() {
        observer.setAction(monitorIndex, this::OnStart);
    }
}
