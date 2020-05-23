public class ReadyChecker {
    int totalNumber;
    int currentNumber = 0;

    boolean[] windowsReady;
    SwitchedPanel[] panels;

    public ReadyChecker(int totalNumber) {
        this.totalNumber = totalNumber;
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
            for(SwitchedPanel panel : panels) {
                panel.switchPanel("card");
                //panel.setActionForIntroPanel();
            }
        }
    }
}
