import gui.view.MonologueBar;
import gui.view.StatusBar;

import javax.swing.*;
import java.awt.*;

public class SwitchedPanel extends JPanel {
    int index;
    GeneralLogic generalLogic;
    StatusBar downBar;

    IntroPanel intro;
    CardPanel cardPanel;
    MonologueBar monologueBar;

    public SwitchedPanel(int index,
                         GeneralLogic generalLogic,
                         StatusBar downBar,
                         MonologueBar monologueBar) {
        this.index = index;
        this.generalLogic = generalLogic;
        this.downBar = downBar;
        this.monologueBar = monologueBar;
        setLayout(new CardLayout());

        intro = new IntroPanel(index, generalLogic, this, monologueBar );
        cardPanel = new CardPanel(index, generalLogic, downBar);

        add(intro, "intro");
        add(cardPanel, "card");
    }

    public void switchPanel(String key) {
        CardLayout cardLayout = (CardLayout)getLayout();
        cardLayout.show(this, key);
    }

    public void setOnStartAction() {
        intro.setOnStartAction();
    }
}
