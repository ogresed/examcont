import java.util.TreeMap;

public class DeviceBinder {
    private BindMicePanel bindPanel;
    TreeMap<Integer, Integer> windowDevise;
    int currentWindowToBind;

    public void setBindPanel(BindMicePanel bindPanel) {
        this.bindPanel = bindPanel;
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
        bindPanel.sayDone(currentWindowToBind ,deviceIndex);
        currentWindowToBind = -1;
    }
}
