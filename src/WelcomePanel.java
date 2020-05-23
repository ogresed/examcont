import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    WelcomeFrame mainFrame;

    public WelcomePanel(WelcomeFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
