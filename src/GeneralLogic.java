import gui.card.Collage;
import gui.card.CollageBuilder;
import utils.Constants;

import java.awt.image.BufferedImage;
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
            Role role = gameState.roundsGlobalState.getRole(frame.index);
            if(role != Role.Questioner && role != Role.Suggester) {
                frame.monologueBar.setText(message);
            }
        }
    }

    public void sendSuggester(String message) {
        playersFrames.get(gameState.roundsGlobalState.getSuggester()).monologueBar.setText(message);
    }

    public void sendQuestioner(String message) {
        playersFrames.get(gameState.roundsGlobalState.getQuestioner()).monologueBar.setText(message);
    }

    public void sendWhoNotQuestioner(String message) {
        for(PlayersFrame frame: playersFrames) {
            Role role = gameState.roundsGlobalState.getRole(frame.index);
            if(role != Role.Questioner) {
                frame.monologueBar.setText(message);
            }
        }
    }

    public void nextLetter() {
        gameState.numberOfLetters++;
        String prefix = gameState.getPrefix();
        BufferedImage collageImage = collageBuilder.createCollage(prefix);

        for(PlayersFrame frame : playersFrames) {
            frame.switchedPanel.cardPanel.setPicture(collageImage);
        }
    }
}
