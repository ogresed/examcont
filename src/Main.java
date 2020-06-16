import gui.card.CollageBuilder;

import javax.swing.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        WelcomeFrame welcomeFrame = new WelcomeFrame();
        CollageBuilder.collageWidth = 5;
        CollageBuilder.collageHeight = 4;
        CollageBuilder.collageSize = 20;
        //
        for(int i = 0; i < welcomeFrame.playersFrames.size(); i++) {
            welcomeFrame.panel.deviceBinder.windowDevise.put(i,i);
        }
        welcomeFrame.onUndecore();
        welcomeFrame.onFullScreen();
        welcomeFrame.onRun();
    }
}