import coordinates.Coordinates;
import manymouse.ClickButtonAction;
import manymouse.ManyMouseEvent;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ManyMouseObserver {
    /**
     * singleton realization
     * */
    private static ManyMouseObserver instance;

    private ManyMouseObserver() {
        clickActions = new TreeMap<>();
        new Thread(this::observe).start();
    }

    public static synchronized ManyMouseObserver getInstance () {
        if(instance == null) {
            instance = new ManyMouseObserver();
        }
        return instance;
    }
    /**
     * end of singleton realization
     */
    private int numberOfMice;
    private ArrayList<Coordinates> coordinates;
    private Map<Integer, ClickButtonAction> clickActions;
    DeviceRecorder deviceRecorder;
    TreeMap<Integer, Integer> map;

    public void setMap(TreeMap<Integer, Integer> map) {
        this.map = map;
    }

    public void setAction(int index, ClickButtonAction.Action action) {
        ClickButtonAction buttonAction = clickActions.get(index);
        if(buttonAction == null) {
            buttonAction = new ClickButtonAction();
            clickActions.put(index, buttonAction);
        }
        buttonAction.setAction(action);
    }

    public int getNumberOfMice() {
        return numberOfMice;
    }

    public Coordinates getCoordinates(int index) {
        return coordinates.get(index);
    }

    private void observe() {
        numberOfMice = ManyMouse.Init();
        coordinates = new ArrayList<>(numberOfMice);
        for(int i = 0; i < numberOfMice; i++) {
            coordinates.add(new Coordinates());
        }

        ManyMouseEvent event = new ManyMouseEvent();
        while (numberOfMice > 0) {
            if (!ManyMouse.PollEvent(event))
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            else {
                switch (event.type) {
                    case ManyMouseEvent.RELMOTION:
                        changeCoordinates(event);
                        break;
                    case ManyMouseEvent.BUTTON:
                        //0 - release, 1 - press
                        if (event.value == 0) {
                            int deviceIndex = event.device;
                            goAction(deviceIndex);
                        } else if(event.value == 1) {
                            if(deviceRecorder.isToRecord()) {
                                deviceRecorder.setSignal(event.device);
                            }
                        }
                        break;
                    case ManyMouseEvent.DISCONNECT:
                        numberOfMice--;
                        break;
                    default :
                        break;
                }
            }
        }
        ManyMouse.Quit();
    }

    private void goAction(int deviceIndex) {
        Coordinates c = coordinates.get(deviceIndex);
        int mIndex = getMIndex(deviceIndex);
        try {clickActions.get(mIndex).getAction().action(c.getX(), c.getY());}catch(NullPointerException ignore){}
    }

    private int getMIndex(int deviceIndex) {
        for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if(entry.getValue() == deviceIndex) {
                return entry.getKey();
            }
        }
        return -1;
    }

    private void changeCoordinates(ManyMouseEvent event) {
        // witch mouse
        int device = event.device;
        // x or y | if button pressed: left0 or right1
            int item = event.item;
        // offset
        int value = event.value;
        Coordinates mouseCoordinates = coordinates.get(device);
        // x
        if (item == 0) {
            int x = mouseCoordinates.getX() + value;
            int max = mouseCoordinates.getXmax();
            x = checkX(x, max);
            mouseCoordinates.setX(x);
        }
        // y
        else if (item == 1) {
            int y = mouseCoordinates.getY() + value;
            int max = mouseCoordinates.getYmax();
            y = checkY(y, max);
            mouseCoordinates.setY(y);
        }
    }

    private int checkX(int x, int max) {
        if (x < 0) {
            return 0;
        }
        if (x > max) {
            return max - 45;
        }
        return x;
    }

    private int checkY(int y, int max) {
        if (y < 0) {
            return 0;
        }
        if (y > max) {
            return max - 45;
        }
        return y;
    }

    public DeviceRecorder getDeviceRecorder() {
        return deviceRecorder;
    }

    public void setDeviceRecorder(DeviceRecorder deviceRecorder) {
        this.deviceRecorder = deviceRecorder;
    }
}
