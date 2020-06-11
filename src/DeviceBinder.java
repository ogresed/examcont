import java.util.TreeMap;

public class DeviceBinder {
    private WelcomePanel panel;
    TreeMap<Integer, Integer> windowDevise;
    int currentWindowToBind;

    public void setPanel(WelcomePanel panel) {
        this.panel = panel;
    }

    public DeviceBinder () {
        windowDevise = new TreeMap<>();
    }

    public TreeMap<Integer, Integer> getWindowDevise() {
        return windowDevise;
    }

    public void setWindow(int monitorIndex) {
        currentWindowToBind = monitorIndex;
    }

    public void addBind(int deviceIndex) {
        windowDevise.put(currentWindowToBind, deviceIndex);
        panel.sayDone(currentWindowToBind ,deviceIndex);
        currentWindowToBind = -1;
    }
}
