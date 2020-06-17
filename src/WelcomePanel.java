import gui.card.CollageBuilder;
import gui.view.MonologueBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class WelcomePanel extends JPanel {
    private static final String introForBindMice = "Выберите номер окна чтобы связать его с девайсом. Для связывания щёлкните мышкой столько раз: ";
    private static final String intro = "Для привязки мыши к монитору выберите монитор, затем кликните связываемой мышью несколько раз";
    MonologueBar monologue;
    JRadioButton[] jRadioButtons;

    int numberOfMice;
    ArrayList<PlayersFrame> playersFrames;
    DeviceBinder deviceBinder;
    DeviceCounter deviceRecorder;

    JPanel bindMicePanel = new JPanel();
    JPanel collageSizePanel = new JPanel();
    JPanel settingsPanel = new JPanel();

    public WelcomePanel(int numberOfMice, ArrayList<PlayersFrame> playersFrames, DeviceBinder deviceBinder, DeviceCounter deviceRecorder) {
        this.numberOfMice = numberOfMice;
        this.playersFrames = playersFrames;
        this.deviceBinder = deviceBinder;
        this.deviceRecorder = deviceRecorder;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown() ) {
                    System.exit(0);
                }
            }
        });

        setLayout(new BorderLayout());
        monologue = new MonologueBar(300, 100, 15);
        monologue.setText(intro);
        //panels layout
        add(monologue, BorderLayout.NORTH);
        add(settingsPanel, BorderLayout.CENTER);
        settingsPanel.setLayout(new GridLayout(2, 1));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Настройки"));
        bindMicePanel.setLayout(new GridLayout(2, 1));
        bindMicePanel.setBorder(BorderFactory.createTitledBorder("Связывание мыши и экрана"));
        collageSizePanel.setLayout(new GridLayout(1, 3));
        collageSizePanel.setBorder(BorderFactory.createTitledBorder("Размер коллажа"));
        settingsPanel.add(bindMicePanel);
        settingsPanel.add(collageSizePanel);
        //about bind mice
        createRadioForBindMice();
        createButtonForBindMice();
        //end of about bind mice
        // about size of collage
        createChoseCollageSize();
        // enx of about size of collage
    }

    private void createChoseCollageSize() {
        String[] vertical = new String[]{"2", "3", "4"};
        String[] horizontal = new String[]{"3", "4", "5"};

        DefaultComboBoxModel<String> verticalModel = new DefaultComboBoxModel<>();
        for (String element : vertical) verticalModel.addElement(element);
        DefaultComboBoxModel<String> horizontalModel = new DefaultComboBoxModel<>();
        for (String element : horizontal) horizontalModel.addElement(element);

        JComboBox<String> verticalBox = new JComboBox<>(vertical);
        JComboBox<String> horizontalBox = new JComboBox<>(horizontal);

        JButton btnAdd = new JButton("Установить значения");
        btnAdd.addActionListener(e -> {
            int verticalSize = verticalBox.getSelectedIndex() + 2;
            int horizontalSize = horizontalBox.getSelectedIndex() + 3;
            CollageBuilder.collageHeight = verticalSize;
            CollageBuilder.collageWidth = horizontalSize;
            CollageBuilder.collageSize = verticalSize * horizontalSize;
            monologue.setText("Размеры коллажа установлены");
        });

        JPanel verticalPanel = new JPanel();
        verticalPanel.add(verticalBox);
        verticalPanel.setBorder(BorderFactory.createTitledBorder("Вертикаль"));
        collageSizePanel.add(verticalPanel);

        JPanel horizontalPanel = new JPanel();
        horizontalPanel.add(horizontalBox);
        horizontalPanel.setBorder(BorderFactory.createTitledBorder("Горизонталь"));
        collageSizePanel.add(horizontalPanel);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdd);
        collageSizePanel.add(btnPanel);
    }

    private void createRadioForBindMice() {
        JPanel radioPanel = new JPanel();
        jRadioButtons = new JRadioButton[numberOfMice];
        ButtonGroup buttonGroup = new ButtonGroup();
        for(int i = 0; i < numberOfMice; i++) {
            JRadioButton button = new JRadioButton(String.valueOf(i));
            jRadioButtons[i] = button;
            buttonGroup.add(button);
            radioPanel.add(button);
        }
        bindMicePanel.add(radioPanel, BorderLayout.CENTER);
    }

    private void createButtonForBindMice() {
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
        bindMicePanel.add(buttonPanel, BorderLayout.SOUTH);
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
