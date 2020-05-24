import gui.card.Collage;
import gui.card.CollageBuilder;
import utils.Constants;

import java.util.TreeMap;

public class GeneralLogic {
    public enum Role {
        Answer, Question
    }

    int playersNumber;
    ManyMouseObserver mouseObserver;
    TreeMap<Integer, Integer> windowDevise;
    ReadyChecker readyChecker;
    CollageBuilder collageBuilder;
    Collage collage;
    Role[] roles;

    public GeneralLogic(int numberOfPlayers, ManyMouseObserver mouseObserver, TreeMap<Integer, Integer> windowDevise) {
        this.playersNumber = numberOfPlayers;
        this.mouseObserver = mouseObserver;
        this.windowDevise = windowDevise;
        this.collage = new Collage();
        this.collageBuilder = new CollageBuilder(System.getProperty("user.dir") +
                Constants.RESOURCES_PATH + "\\cards", collage);
        this.readyChecker = new ReadyChecker(numberOfPlayers, collageBuilder);

        roles = new Role[numberOfPlayers];
        roles[0] = Role.Question;
        for(int i = 1; i < numberOfPlayers; i++) {
            roles[i] = Role.Answer;
        }
    }

    public ReadyChecker getReadyChecker() {
        return readyChecker;
    }
}
