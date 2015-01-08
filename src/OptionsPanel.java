import javax.swing.*;
import java.awt.*;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2014-12-31.
 */
public class OptionsPanel extends JPanel {
    Gui gui;
    JPanel camPanel;
    JPanel secondPanel;
    JLabel camL;
    JLabel camCenterL;
    JLabel camLX;
    JLabel camLY;
    JLabel camLZ;
    JLabel camCLX;
    JLabel camCLY;
    JLabel camCLZ;
    JLabel camLAng;

    JTextField camX;
    JTextField camY;
    JTextField camZ;
    JTextField camCX;
    JTextField camCY;
    JTextField camCZ;
    JSlider camSAng;

    JPanel ligPanel;
    JLabel ligLX;
    JLabel ligLY;
    JLabel ligLZ;

    JButton colButton;
    JLabel colPreview;
    JTextField ligX;
    JTextField ligY;
    JTextField ligZ;
    final Dimension SCREEN_SIZE = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();

    JPanel dataPanel;
    JButton setB;
    JButton resetB;
    JButton saveB;
    JButton loadB;


    public OptionsPanel(Gui gui) {
        this.gui = gui;
        this.setLayout(new GridLayout(2, 1, 20, 10));
        this.setMaximumSize(new Dimension(SCREEN_SIZE.width / 7, SCREEN_SIZE.height));
        this.setPreferredSize(new Dimension(SCREEN_SIZE.width / 7, SCREEN_SIZE.height));
        makeComponents();
        makePanels();
        addComponents();
    }

    private void makeComponents() {
        camL = new JLabel("Cam Position:");
        camCenterL = new JLabel("Cam Center:");
        camLX = new JLabel("X Position: ");
        camLY = new JLabel("Y Position: ");
        camLZ = new JLabel("Z Position: ");
        camCLX = new JLabel("X Position: ");
        camCLY = new JLabel("Y Position: ");
        camCLZ = new JLabel("Z Position: ");
        camX = new JTextField("0");
        camY = new JTextField("0");
        camZ = new JTextField("0");
        camCX = new JTextField("0");
        camCY = new JTextField("0");
        camCZ = new JTextField("0");

        camLAng = new JLabel("Cam. Angle: 45");
        camSAng = new JSlider(1, 89, 45);
        camSAng.addChangeListener(gui);

        ligLX = new JLabel("X Position: ");
        ligLY = new JLabel("Y Position: ");
        ligLZ = new JLabel("Z Position: ");
        ligX = new JTextField("0");
        ligY = new JTextField("0");
        ligZ = new JTextField("0");
        colButton = new JButton("Choose Color");
        colPreview = new JLabel("");
        colPreview.setOpaque(true);
        colPreview.setBackground(Color.WHITE);

        setB = new JButton("Set");
        resetB = new JButton("Reset");
        saveB = new JButton("Save");
        loadB = new JButton("Load");

        colButton.addActionListener(gui);
        saveB.addActionListener(gui);
        loadB.addActionListener(gui);
        resetB.addActionListener(gui);
        setB.addActionListener(gui);


        camSAng.setMaximumSize(new Dimension(SCREEN_SIZE.width / 7, camSAng.getPreferredSize().height));
        camSAng.setSize(new Dimension(SCREEN_SIZE.width / 7, camSAng.getPreferredSize().height));
        camX.setMaximumSize(new Dimension(SCREEN_SIZE.width / 7, camX.getPreferredSize().height));
        camY.setMaximumSize(new Dimension(SCREEN_SIZE.width / 7, camY.getPreferredSize().height));
        camZ.setMaximumSize(new Dimension(SCREEN_SIZE.width / 7, camZ.getPreferredSize().height));
        camCX.setMaximumSize(new Dimension(SCREEN_SIZE.width / 7, camCX.getPreferredSize().height));
        camCY.setMaximumSize(new Dimension(SCREEN_SIZE.width / 7, camCY.getPreferredSize().height));
        camCZ.setMaximumSize(new Dimension(SCREEN_SIZE.width / 7, camCZ.getPreferredSize().height));
        ligX.setMaximumSize(new Dimension(SCREEN_SIZE.width / 7, ligX.getPreferredSize().height));
        ligY.setMaximumSize(new Dimension(SCREEN_SIZE.width / 7, ligY.getPreferredSize().height));
        ligZ.setMaximumSize(new Dimension(SCREEN_SIZE.width / 7, ligZ.getPreferredSize().height));

    }

    private void addComponents() {
        add(camPanel, 0);
        add(secondPanel, 1);
        secondPanel.add(ligPanel);
        secondPanel.add(dataPanel);
        camPanel.add(camL);
        camPanel.add(camLX);
        camPanel.add(camX);
        camPanel.add(camLY);
        camPanel.add(camY);
        camPanel.add(camLZ);
        camPanel.add(camZ);

        camPanel.add(camCenterL);
        camPanel.add(camCLX);
        camPanel.add(camCX);
        camPanel.add(camCLY);
        camPanel.add(camCY);
        camPanel.add(camCLZ);
        camPanel.add(camCZ);

        camPanel.add(camLAng);
        camPanel.add(camSAng);

        ligPanel.add(ligLX);
        ligPanel.add(ligX);
        ligPanel.add(ligLY);
        ligPanel.add(ligY);
        ligPanel.add(ligLZ);
        ligPanel.add(ligZ);
        ligPanel.add(colButton);
        ligPanel.add(colPreview);

        dataPanel.add(setB);
        dataPanel.add(resetB);
        dataPanel.add(saveB);
        dataPanel.add(loadB);
    }

    private void makePanels() {
        camPanel = new JPanel();
        camPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.black),
                        "Camera"));
        camPanel.setLayout(new GridLayout(16, 1, 5, 5));

        ligPanel = new JPanel();
        ligPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.black),
                        "Light"));
        ligPanel.setLayout(new GridLayout(8, 1, 5, 5));

        dataPanel = new JPanel(new GridLayout(5, 1, 20, 5));

        secondPanel = new JPanel(new GridLayout(2, 1, 20, 5));


    }
}
