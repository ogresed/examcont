import gui.baseframe.BaseFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WelcomeFrame extends BaseFrame {
    WelcomePanel panel;
    BindMicePanel bindMicePanel;
    JPanel transitPanel;
    ArrayList<PlayersFrame> playersFrames;
    GeneralLogic general;
    public WelcomeFrame() {
        //set base option
        super(JFrame.EXIT_ON_CLOSE, "Contact");
        //todo: полноэкраннй режим
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);
        setBounds(500, 150, 700, 540);
        //
        createButtons();
        //====================================
        // initialization
        ManyMouseObserver mouseObserver = ManyMouseObserver.getInstance();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int numberOfMouse = mouseObserver.getNumberOfMice();
        //TODO: if number of mice == 0 restart or other solution
        //====================================
        Rectangle r = getBounds();
        //initialization
        playersFrames = new ArrayList<>(numberOfMouse);
        general = new GeneralLogic(numberOfMouse);

        //adding panels
        transitPanel = new JPanel();
        panel = new WelcomePanel(this);

        DeviceBinder deviceBinder = new DeviceBinder();

        mouseObserver.setMap(deviceBinder.getWindowDevise());
        DeviceRecorder deviceRecorder = new DeviceRecorder(5, deviceBinder);
        mouseObserver.setDeviceRecorder(deviceRecorder);
        bindMicePanel = new BindMicePanel(numberOfMouse, playersFrames, deviceBinder, deviceRecorder);
        deviceBinder.setBindPanel(bindMicePanel);

        transitPanel.setLayout(new CardLayout());
        transitPanel.add(panel, "main");
        transitPanel.add(bindMicePanel, "bind");
        add(transitPanel, BorderLayout.CENTER);
        //create cards frames
        for(int i = 0; i < numberOfMouse; i++) {
            playersFrames.add(new PlayersFrame(i, mouseObserver, general, deviceBinder.getWindowDevise()));
        }
        //final settings
        setVisible(true);
        revalidate();
    }

    @Override
    protected void createButtons () {
        JMenu file =  makeMenu("File", 'F');
        createAction(file, "pictures/Exit.png", exitListener,'E', "Exit application");
        createAction(file, "pictures/Run.png", runListener,'R', "Run examination");
        toolBar.addSeparator();
        JMenu settings = makeMenu("Settings", 'S');
        createAction(settings, "pictures/SetMouse.png", mouseSetListener,'M', "Mapping mouse with window");
    }

    ActionListener runListener = e -> onRun();
    ActionListener mouseSetListener = e -> onSetMouse();
    ActionListener exitListener = e -> System.exit(0);

    private void onRun() {
        setVisible(false);
        for(PlayersFrame frame : playersFrames) {
            frame.setVisible(true);
            frame.setIntroAction();
        }
    }

    private void onSetMouse() {
        switchPanel("bind");
    }

    public void switchPanel(String key) {
        CardLayout cardLayout = (CardLayout) transitPanel.getLayout();
        cardLayout.show(transitPanel, key);
    }
}
