import gui.card.Card;
import gui.card.CollageBuilder;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ReadyChecker {
    int totalNumber;
    int currentNumber = 0;

    boolean[] windowsReady;
    SwitchedPanel[] panels;
    CollageBuilder collageBuilder;
    GameState gameState;

    public ReadyChecker(int totalNumber, CollageBuilder collageBuilder, GameState gameState) {
        this.totalNumber = totalNumber;
        this.collageBuilder = collageBuilder;
        this.gameState = gameState;
        windowsReady = new boolean[totalNumber];
        panels = new SwitchedPanel[totalNumber];
    }

    //todo параметризованый размер подписи под картинкой

    public void ready(SwitchedPanel switchedPanel, int index) {
        boolean isReady = windowsReady[index];
        if(!isReady) {
            windowsReady[index] = true;
            currentNumber++;
            panels[index] = switchedPanel;
        }
        if(currentNumber == totalNumber) {
            preparationForGame();
        }
    }

    private void preparationForGame() {
        int x = Integer.MAX_VALUE, y = Integer.MAX_VALUE;
        for(SwitchedPanel panel : panels) {
            Rectangle r = panel.playersFrame.getBounds();
            if(r.height < y) y = r.height;
            if(r.width < x) x = r.width;
        }
        y -= (PlayersFrame.maxMonoY + PlayersFrame.statusBarY);
        Card.width = x / CollageBuilder.collageWidth;
        Card.height = y / CollageBuilder.collageHeight;
        System.out.println(x + "  " + y);
        String prefix = "по";
        BufferedImage collage = collageBuilder.createCollage(prefix);
//todo : main panel - панель настроек - связывание мышек, выбор префикса, выбор размера коллажа
        gameState.setCurrentWhoCanClick(WhoCanClick.Questioner);

        for(SwitchedPanel panel : panels) {
            panel.cardPanel.setChooseAction();
            panel.cardPanel.setPicture(collage);
            if(gameState.getRole(panel.index) == Role.Answer) {
                panel.monologueBar.setText(CardPanel.WAIT_FOR_CHOOSE_CARD);
            } else if(gameState.getRole(panel.index) == Role.Question) {
                panel.monologueBar.setText(CardPanel.CHOOSE_CARD);
            }
            panel.switchPanel("card");
        }
    }
}
