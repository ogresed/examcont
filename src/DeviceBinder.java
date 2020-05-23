import java.util.TreeMap;

public class DeviceBinder {
    private BindMicePanel bindPanel;

    public BindMicePanel getBindPanel() {
        return bindPanel;
    }

    public void setBindPanel(BindMicePanel bindPanel) {
        this.bindPanel = bindPanel;
    }

    public DeviceBinder () {
        windowDevise = new TreeMap<>();
    }

    TreeMap<Integer, Integer> windowDevise;

    public TreeMap<Integer, Integer> getWindowDevise() {
        return windowDevise;
    }

    int currentWindowToBind;

    public void setWindow(int monitorIndex) {
        currentWindowToBind = monitorIndex;
    }

    public void addBind(int deviceIndex) {
        windowDevise.put(currentWindowToBind, deviceIndex);
        bindPanel.sayDone(currentWindowToBind ,deviceIndex);
        currentWindowToBind = -1;
    }

    public int getDevice(int monitorIndex) {
        return windowDevise.get(monitorIndex);
    }
}
