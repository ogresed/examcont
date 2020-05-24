import java.awt.*;
import java.util.ArrayList;

public class WindowOption {
    public static Toolkit toolkit;
    public static Dimension dimension;
    public static GraphicsEnvironment graphicsEnvironment;
    public static ArrayList <GraphicsDevice> screenDevises;
    public static int numDisplays;

    static {
        toolkit = Toolkit.getDefaultToolkit();
        dimension = toolkit.getScreenSize();

        graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
    }
}
