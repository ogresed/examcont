public class ManyMouse {
    // Native method hooks.
    public native static synchronized int Init();
    public native static synchronized void Quit();
    public native static synchronized String DeviceName(int index);
    public native static synchronized boolean PollEvent(ManyMouseEvent event);

    // JNI link.
    private static final String LIB_PATH = System.getProperty("user.dir") + "\\resources\\native\\";
    private static final String LIB_NAME = "ManyMouse.dll";

    static {
        System.load(LIB_PATH + LIB_NAME);
    }
}

