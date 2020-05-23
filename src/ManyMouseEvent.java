public class ManyMouseEvent {
    // Event types...
    public static final int ABSMOTION = 0;
    public static final int RELMOTION = 1;
    public static final int BUTTON = 2;
    public static final int SCROLL = 3;
    public static final int DISCONNECT = 4;

    public int type;
    public int device;
    public int item;
    public int value;

    public int minval;
    public int maxval;
}

