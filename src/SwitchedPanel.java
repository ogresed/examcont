import javax.swing.*;
import java.awt.*;
import java.util.TreeMap;

public class SwitchedPanel extends JPanel {
    int index;
    ManyMouseObserver observer;
    GeneralLogic generalLogic;
    StatusBar downBar;

    IntroPanel intro;
    CardPanel cardPanel;
    MonologueBar monologueBar;

    public SwitchedPanel(int index,
                         ManyMouseObserver observer,
                         GeneralLogic generalLogic,
                         StatusBar downBar,
                         MonologueBar monologueBar, TreeMap<Integer, Integer> map) {
        this.index = index;
        this.observer = observer;
        this.generalLogic = generalLogic;
        this.downBar = downBar;
        this.monologueBar = monologueBar;
        setLayout(new CardLayout());

        intro = new IntroPanel(generalLogic.getReadyChecker(), index, this, observer, map, monologueBar);
        cardPanel = new CardPanel(index, observer, generalLogic, downBar, map);

        add(intro, "intro");
        add(cardPanel, "card");
    }

    public void switchPanel(String key) {
        CardLayout cardLayout = (CardLayout)getLayout();
        cardLayout.show(this, key);
    }

    public void setActionForIntroPanel() {
        intro.setAction();
    }
}
