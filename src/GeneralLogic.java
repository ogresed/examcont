import gui.card.Collage;
import gui.card.CollageBuilder;
import utils.Constants;

import java.util.ArrayList;
import java.util.TreeMap;

public class GeneralLogic {
    int playersNumber;
    boolean clickable = false;
    ManyMouseObserver mouseObserver;
    TreeMap<Integer, Integer> windowDevise;
    ReadyChecker readyChecker;
    CollageBuilder collageBuilder;
    Collage collage;
    GameState gameState;
    ArrayList<PlayersFrame> playersFrames;

    public GeneralLogic(int numberOfPlayers, ManyMouseObserver mouseObserver,
                        TreeMap<Integer, Integer> windowDevise, ArrayList<PlayersFrame> playersFrames) {
        this.playersNumber = numberOfPlayers;
        this.mouseObserver = mouseObserver;
        this.windowDevise = windowDevise;
        this.playersFrames = playersFrames;
        this.collage = new Collage();
        this.collageBuilder = new CollageBuilder(System.getProperty("user.dir") +
                Constants.RESOURCES_PATH + "\\cards", collage);
        this.gameState = new GameState(numberOfPlayers);
        this.readyChecker = new ReadyChecker(numberOfPlayers, collageBuilder, gameState);
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public void sendAllAnswerers(String message) {
        for(PlayersFrame frame: playersFrames) {
            if( gameState.roles[frame.index] == Role.Answerer) {
                frame.monologueBar.setText(message);
            }
        }
    }

    public void sendAllWithoutSuggester(String message) {
        for(PlayersFrame frame: playersFrames) {
            if(frame.index != gameState.suggester) {
                frame.monologueBar.setText(message);
            }
        }
    }

    public void sendQuestioner(String message) {
        playersFrames.get( gameState.questioner).monologueBar.setText(message);
    }
}
