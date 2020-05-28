package gui.view;

import javax.swing.*;
import java.awt.*;

public class MonologueBar extends JLabel {
    public MonologueBar (int width, int height) {
        setOpaque(true);
        setForeground(Color.BLUE);
        setFont(new Font(null, Font.BOLD, 25));
        setHorizontalAlignment(JLabel.LEFT);
        setVerticalAlignment(JLabel.TOP);
        setBackground(new Color(150,250,250));
        Dimension d = new Dimension(width + 1000, height);
        setMaximumSize(d);
        setPreferredSize(d);
        setMinimumSize(d);
        setLocation(0, 0);
    }
    @Override
    public void setText(String text) {
        text = "<html>" + text + "</html>";
        super.setText(text);
    }
}