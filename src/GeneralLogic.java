import gui.card.CollageBuilder;
import utils.Constants;

import java.util.TreeMap;

public class GeneralLogic {
    int miceNumber;
    ManyMouseObserver mouseObserver;
    TreeMap<Integer, Integer> windowDevise;
    ReadyChecker readyChecker;
    CollageBuilder collageBuilder;

    public GeneralLogic(int numberOfMouse, ManyMouseObserver mouseObserver, TreeMap<Integer, Integer> windowDevise) {
        this.miceNumber = numberOfMouse;
        this.mouseObserver = mouseObserver;
        this.windowDevise = windowDevise;
        this.readyChecker = new ReadyChecker(numberOfMouse);
        collageBuilder = new CollageBuilder(System.getProperty("user.dir") +
                Constants.RESOURCES_PATH + "\\cards");
    }

    public ReadyChecker getReadyChecker() {
        return readyChecker;
    }
}
