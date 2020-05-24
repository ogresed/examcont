public class DeviceRecorder {
    private boolean toRecord = false;
    int [] signals;
    public int capacity;
    int current;
    DeviceBinder deviceBinder;

    public DeviceRecorder (int capacity, DeviceBinder deviceBinder) {
        this.capacity = capacity;
        this.deviceBinder = deviceBinder;

        signals = new int[capacity];
    }

    public void setSignal(int fromDevice) {
        signals[current++] = fromDevice;
        if(current == capacity) {
            int deviseIndex = getDevice();
            deviceBinder.addBind(deviseIndex);
            current = 0;
        }
    }

    // каписити - число сигналов для достижения уверенности в выборе девайса
    // девайса обычно имеют индексы 0 1 2 и тд, но это не точно
    private int getDevice() {
        int device = -1;
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
