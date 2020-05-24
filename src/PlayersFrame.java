import coordinates.Coordinates;
import gui.view.MonologueBar;
import gui.view.StatusBar;
import gui.view.baseframe.BaseFrame;
import utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class PlayersFrame extends JFrame {
    int index;
    GeneralLogic general;
    StatusBar downBar;
    MonologueBar monologueBar;
    SwitchedPanel switchedPanel;

    public static BufferedImage cursor;

    static {
        try {
            cursor = ImageIO.read(new File(
                    System.getProperty("user.dir") +
                            Constants.RESOURCES_PATH + "\\pictures\\Mouse_pointer_small.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    PlayersFrame(int index, GeneralLogic generalLogic) {
        //base options
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setBounds(index* 700, 0, 600, 540);
        // definition
        this.index = index;
        this.general = generalLogic;
        this.downBar = new StatusBar(String.valueOf(index));
        this.monologueBar = new MonologueBar(0, 0, 300, 200);
        // resize listener to change max coordinates values
        addComponentListener(new PlayersFrameComponentAdapter());
        // create panels
        setLayout(new BorderLayout());
        switchedPanel = new SwitchedPanel(index, generalLogic, downBar, monologueBar);

        add(monologueBar, BorderLayout.NORTH);
        add(switchedPanel, BorderLayout.CENTER);
        add(downBar, BorderLayout.SOUTH);

        //final settings
        setBlankCursor();
        setVisible(false);
        revalidate();
    }

    private void setBlankCursor() {
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        getContentPane().setCursor(blankCursor);
    }

    public void setOnStartAction() {
        switchedPanel.setOnStartAction();
    }

    class PlayersFrameComponentAdapter extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            Rectangle r = getBounds();
            Coordinates c = general.mouseObserver.getCoordinates(index);
            c.setXmax(r.width);
            c.setYmax(r.height);
        }
    }
}
