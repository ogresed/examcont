package gui.view;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JLabel {
    /** Creates a new instance of StatusBar */
    StatusBar() {
        super();
        super.setPreferredSize(new Dimension(200, 16));
    }

    public StatusBar(String text) {
        super();
        super.setPreferredSize(new Dimension(200, 16));
        setText(text);
    }

    public StatusBar(int w, int h) {
        super();
        super.setPreferredSize(new Dimension(w, h));
    }

    StatusBar(int w, int h, String text) {
        super();
        super.setPreferredSize(new Dimension(w, h));
        setText(text);
    }

    public void setMessage(String string) {
        setText(string);
    }
}