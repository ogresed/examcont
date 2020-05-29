import gui.view.baseframe.BaseFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WelcomeFrame extends BaseFrame {
    WelcomePanel panel;
    public BindMicePanel bindMicePanel;
    JPanel transitPanel;
    ArrayList<PlayersFrame> playersFrames;
    GeneralLogic general;
    DeviceRecorder deviceRecorder;

    public WelcomeFrame() {
        //set base option
        super(JFrame.EXIT_ON_CLOSE, "Contact");
        setBounds(500, 150, 700, 540);
        //
        createButtons();
        //====================================
        // initialization
        ManyMouseObserver mouseObserver = ManyMouseObserver.getInstance();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int numberOfMouse = mouseObserver.getNumberOfMice();
        //todo: if number of mice == 0 restart or other solution
        //====================================
        //initialization
        playersFrames = new ArrayList<>(numberOfMouse);

        //adding panels
        transitPanel = new JPanel();
        panel = new WelcomePanel(this);

        DeviceBinder deviceBinder = new DeviceBinder();

        mouseObserver.setMap(deviceBinder.getWindowDevise());
        deviceRecorder = new DeviceRecorder(6, deviceBinder);
        mouseObserver.setDeviceRecorder(deviceRecorder);
        bindMicePanel = new BindMicePanel(numberOfMouse, playersFrames, deviceBinder, deviceRecorder);
        deviceBinder.setBindPanel(bindMicePanel);

        transitPanel.setLayout(new CardLayout());
        transitPanel.add(panel, "main");
        transitPanel.add(bindMicePanel, "bind");
        add(transitPanel, BorderLayout.CENTER);
        // create general
        general = new GeneralLogic(numberOfMouse, mouseObserver, deviceBinder.getWindowDevise(), playersFrames);
        //create cards frames
        for(int i = 0; i < numberOfMouse; i++) {
            playersFrames.add(new PlayersFrame(i, general));
        }
        //final settings
        setVisible(true);
        revalidate();
    }
    // todo: найти либу для работы СОМ-портом
    @Override
    protected void createButtons () {
        JMenu file =  makeMenu("File", 'F');
        createAction(file, "pictures/Exit.png", exitListener,'E', "Exit application");
        createAction(file, "pictures/Run.png", runListener,'R', "Run examination");
        createAction(file, "pictures/Close.png", closeListener,'C', "Close");
        toolBar.addSeparator();
        JMenu settings = makeMenu("Settings", 'S');
        createAction(settings, "pictures/SetMouse.png", mouseSetListener,'M', "Mapping mouse with window");
        toolBar.addSeparator();
        createAction(settings, "pictures/Undecore.png", undecoreListener,'U', "Maje all windows undecored");
        createAction(settings, "pictures/Full.png", fullScreenListener,'L', "Make all windows full");
    }
    // todo: ставить каждый фрейм в отдельный монитор
    ActionListener runListener = e -> onRun();
    ActionListener mouseSetListener = e -> onSetMouse();
    ActionListener exitListener = e -> System.exit(0);
    ActionListener undecoreListener = e -> onUndecore();
    ActionListener fullScreenListener = e -> onFullScreen();
    ActionListener closeListener = e -> setVisible(false);

    void onUndecore() {
        for(PlayersFrame frame : playersFrames) {
            frame.dispose();
            frame.setUndecorated(true);
            //frame.pack();
            frame.setVisible(true);
        }
    }
    void onFullScreen() {
        for(PlayersFrame frame : playersFrames) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    void onRun() {
        deviceRecorder.setToRecord(false);
        setVisible(false);
        general.setClickable(true);
        for(PlayersFrame frame : playersFrames) {
            frame.setVisible(true);
            frame.setOnStartAction();
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
