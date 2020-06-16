import gui.view.MonologueBar;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BindMicePanel extends JPanel {
    private static final String intro = "Выберите номер окна чтобы связать его с девайсом. Для связывания щёлкните мышкой столько раз: ";
    MonologueBar monologue;
    JRadioButton[] jRadioButtons;
    ArrayList<PlayersFrame> playersFrames;
    DeviceCounter deviceRecorder;
    public DeviceBinder deviceBinder;
    int numberOfMice;
    public BindMicePanel(int numberOfMice, ArrayList<PlayersFrame> playersFrames, DeviceBinder deviceBinder, DeviceCounter recorder) {
        this.playersFrames = playersFrames;
        this.numberOfMice = numberOfMice;
        this.deviceBinder = deviceBinder;
        this.deviceRecorder = recorder;

        setLayout(new BorderLayout());
        //monologue = new MonologueBar(300, 200);
        monologue.setText(intro + deviceRecorder.capacity);
        add(monologue, BorderLayout.NORTH);

        JPanel radioPanel = new JPanel();
        jRadioButtons = new JRadioButton[numberOfMice];
        ButtonGroup buttonGroup = new ButtonGroup();
        for(int i = 0; i < numberOfMice; i++) {
            JRadioButton button = new JRadioButton(String.valueOf(i));
            jRadioButtons[i] = button;
            buttonGroup.add(button);
            radioPanel.add(button);
        }
        add(radioPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton selectButton = new JButton("Выбрать");
        selectButton.addActionListener(l -> {
            for(int i = 0; i < numberOfMice; i++) {
                if(jRadioButtons[i].isSelected()) {
                    monologue.setText("Окно #" + i + " связывается");
                    activateWindow(i);
                    break;
                }
            }
            deviceRecorder.setToRecord(true);
        });
        buttonPanel.add(selectButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void activateWindow(int monitorIndex) {
        deviceBinder.setWindow(monitorIndex);
        for(int i = 0; i < numberOfMice; i++) {
            if(i == monitorIndex) {
                playersFrames.get(i).setVisible(true);
            } else {
                playersFrames.get(i).setVisible(false);
            }
        }
    }

    public void sayDone(int currentWindowToBind, int deviceIndex) {
        monologue.setText("Окно #" + currentWindowToBind + " связано с девайсом #" + deviceIndex);
    }
}
