import java.util.Arrays;

public class DeviceCounter {
    private boolean toRecord = false;
    int [] signals;
    public int capacity;
    int current;
    DeviceBinder deviceBinder;

    public DeviceCounter(int capacity, DeviceBinder deviceBinder) {
        this.capacity = capacity;
        this.deviceBinder = deviceBinder;

        signals = new int[capacity];
        Arrays.fill(signals, -1);
    }

    public void setSignal(int fromDevice) {
        signals[current] = fromDevice;
        current = (current + 1) % capacity;
        int et = signals[0];
        boolean ready = true;
        for(int signal: signals) {
            if(et != signal)
                ready = false;
        }
        if(ready) {
            deviceBinder.addBind(et);
            toRecord = false;
        }
    }

    // каписити - число сигналов для достижения уверенности в выборе девайса
    private int getDevice() {
        int device = -2;
        int [] fre = new int[capacity];
        for(int i = 0; i < capacity; i++) {
            int signal = signals[i];
            fre[signal]++;
        }
        int freq = -1;
        for(int i = 0; i < capacity; i++) {
            if(freq < fre[i]){
                freq = fre[i];
                device = i;
            }
        }
        return device;
    }

    public boolean isToRecord() {
        return toRecord;
    }

    public void setToRecord(boolean toRecord) {
        this.toRecord = toRecord;
    }
}
