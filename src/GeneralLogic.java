import gui.card.Collage;
import gui.card.CollageBuilder;
import utils.Constants;

import java.util.TreeMap;

public class GeneralLogic {
    int playersNumber;
    ManyMouseObserver mouseObserver;
    TreeMap<Integer, Integer> windowDevise;
    ReadyChecker readyChecker;
    CollageBuilder collageBuilder;
    Collage collage;
    GameState gameState;

    public GeneralLogic(int numberOfPlayers, ManyMouseObserver mouseObserver, TreeMap<Integer, Integer> windowDevise) {
        this.playersNumber = numberOfPlayers;
        this.mouseObserver = mouseObserver;
        this.windowDevise = windowDevise;
        this.collage = new Collage();
        this.collageBuilder = new CollageBuilder(System.getProperty("user.dir") +
                Constants.RESOURCES_PATH + "\\cards", collage);
        this.readyChecker = new ReadyChecker(numberOfPlayers, collageBuilder);
        this.gameState = new GameState(numberOfPlayers);
    }

    public ReadyChecker getReadyChecker() {
        return readyChecker;
    }
}
