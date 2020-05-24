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

    public ReadyChecker(int totalNumber, CollageBuilder collageBuilder) {
        this.totalNumber = totalNumber;
        this.collageBuilder = collageBuilder;
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
            BufferedImage collage = collageBuilder.createCollage("п");
            for(SwitchedPanel panel : panels) {
                Rectangle r = panel.getBounds();
                panel.cardPanel.setChooseAction();
                panel.cardPanel.setPicture(collage);
                panel.switchPanel("card");
            }
        }
    }
}
