import gui.card.CollageBuilder;

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

    public void ready(SwitchedPanel switchedPanel, int index) {
        boolean isReady = windowsReady[index];
        if(!isReady) {
            windowsReady[index] = true;
            currentNumber++;
            panels[index] = switchedPanel;
        }
        if(currentNumber == totalNumber) {
            BufferedImage collage = collageBuilder.createCollage("п");
            for(SwitchedPanel panel : panels) {
                panel.cardPanel.setChooseAction();
                panel.cardPanel.setPicture(collage);
                panel.switchPanel("card");
            }
        }
    }
}
