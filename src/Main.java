public class Main {
    public static void main(String[] args) {
        WelcomeFrame welcomeFrame = new WelcomeFrame();

        //
        for(int i = 0; i < welcomeFrame.playersFrames.size(); i++) {
            welcomeFrame.bindMicePanel.deviceBinder.windowDevise.put(i,i);
        }
        welcomeFrame.onUndecore();
        welcomeFrame.onFullScreen();
        welcomeFrame.onRun();
    }
}