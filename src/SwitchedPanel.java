import gui.view.MonologueBar;
import gui.view.StatusBar;

import javax.swing.*;
import java.awt.*;

public class SwitchedPanel extends JPanel {
    int index;
    GeneralLogic generalLogic;
    StatusBar downBar;
    public PlayersFrame playersFrame;

    IntroPanel intro;
    CardPanel cardPanel;
    public MonologueBar monologueBar;

    public SwitchedPanel(int index,
                         GeneralLogic generalLogic,
                         StatusBar downBar,
                         MonologueBar monologueBar,
                         PlayersFrame playersFrame) {
        this.index = index;
        this.generalLogic = generalLogic;
        this.downBar = downBar;
        this.monologueBar = monologueBar;
        this.playersFrame = playersFrame;
        setLayout(new CardLayout());

        intro = new IntroPanel(index, generalLogic, this, monologueBar );
        cardPanel = new CardPanel(index, generalLogic, downBar, monologueBar);

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
