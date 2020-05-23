import javax.swing.*;
import java.awt.*;

public class MonologueBar extends JLabel {
    public MonologueBar (int x, int y, int width, int height) {
        setOpaque(true);
        setForeground(Color.BLUE);
        setFont(new Font(null, Font.BOLD, 25));
        setHorizontalAlignment(JLabel.LEFT);
        setVerticalAlignment(JLabel.TOP);
        setBackground(new Color(250,250,250));
        setBounds(x, y, width, height);
    }

    @Override
    public void setText(String text) {
        text = "<html>" + text + "</html>";
        super.setText(text);
    }
}