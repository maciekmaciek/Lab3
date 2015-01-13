import Jama.Matrix;
import javafx.geometry.Point3D;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2014-12-31.
 */
public class Gui implements ChangeListener, ActionListener {
    JFrame appFrame;
    OrtoPanel xPanel;
    OrtoPanel yPanel;
    OrtoPanel zPanel;
    PerspectivePanel pPanel;
    OptionsPanel optPanel;
    JPanel viewsPanel;
    String CURRENT_FILE_PATH;
    MyMouseAdapter myMouseAdapter;
    Pyramid pyramid;
    final Dimension SCREEN_SIZE = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
    DrawnData drawnData;
    DrawnData normalizedData;
    public Matrix currentTransform;
    public Matrix worldToView;
    public final Color BCG_COLOR = Color.DARK_GRAY;


    public Gui() {
        myMouseAdapter = new MyMouseAdapter();
        appFrame = new JFrame("Lab3");
        appFrame.setSize(SCREEN_SIZE);
        appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        appFrame.setLayout(new BorderLayout());
        makeComponents();
        addComponents();
        appFrame.setVisible(true);
        pyramid = new Pyramid((double) pPanel.getHeight() / (double) pPanel.getWidth());
        System.out.print((double) pPanel.getHeight() / (double) pPanel.getWidth());
    }

    private void addComponents() {
        viewsPanel.add(xPanel);
        viewsPanel.add(yPanel);
        viewsPanel.add(zPanel);
        viewsPanel.add(pPanel);
        appFrame.getContentPane().add(viewsPanel, BorderLayout.CENTER);
        appFrame.getContentPane().add(optPanel, BorderLayout.EAST);


    }

    private void makeComponents() {
        optPanel = new OptionsPanel(this);
        viewsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        xPanel = new OrtoPanel("X", this);
        yPanel = new OrtoPanel("Y", this);
        zPanel = new OrtoPanel("Z", this);
        pPanel = new PerspectivePanel(this);

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        optPanel.camLAng.setText("Cam. Angle: " + optPanel.camSAng.getValue());
        pyramid.getCamera().setAngle(optPanel.camSAng.getValue());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == optPanel.colButton) {
            Color lightColor = JColorChooser.showDialog(
                    optPanel,
                    "Choose Light",
                    optPanel.colPreview.getBackground());
            if (lightColor != null) {
                optPanel.colPreview.setBackground(lightColor);
                pyramid.light.setColor(lightColor);
            }
        } else if (e.getSource() == optPanel.resetB) {
            if (CURRENT_FILE_PATH != null) {
                drawnData = DataManager.loadData(CURRENT_FILE_PATH);
                setValues();
            }

        } else if (e.getSource() == optPanel.loadB) {
            JFileChooser jfc = new JFileChooser();
            int returnVal = jfc.showOpenDialog(appFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                CURRENT_FILE_PATH = jfc.getSelectedFile().getPath();
                drawnData = DataManager.loadData(CURRENT_FILE_PATH);
                setValues();
            }

        } else if (e.getSource() == optPanel.saveB) {
            JFileChooser jfc = new JFileChooser();
            int returnVal = jfc.showSaveDialog(appFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String newPath = jfc.getSelectedFile().getPath();
                DataManager.saveData(CURRENT_FILE_PATH, newPath, pyramid.getLight(), pyramid.getCamera());
                CURRENT_FILE_PATH = newPath;
            }
        } else if (e.getSource() == optPanel.setB) {
            setValuesFromText();
        }
    }

    private void setValues() {
        optPanel.camX.setText(Integer.toString((int) drawnData.camera.getPosition().getX()));
        optPanel.camY.setText(Integer.toString((int) drawnData.camera.getPosition().getY()));
        optPanel.camZ.setText(Integer.toString((int) drawnData.camera.getPosition().getZ()));
        optPanel.camCX.setText(Integer.toString((int) drawnData.camera.getCenter().getX()));
        optPanel.camCY.setText(Integer.toString((int) drawnData.camera.getCenter().getY()));
        optPanel.camCZ.setText(Integer.toString((int) drawnData.camera.getCenter().getZ()));
        optPanel.camSAng.setValue(drawnData.camera.getAngle());

        optPanel.ligX.setText(Integer.toString((int) drawnData.light.getX()));
        optPanel.ligY.setText(Integer.toString((int) drawnData.light.getY()));
        optPanel.ligZ.setText(Integer.toString((int) drawnData.light.getZ()));
        setValuesFromText();
    }


    private void setValuesFromText() {
        pyramid.getCamera().setPosition(new Point3D(
                Integer.parseInt(optPanel.camX.getText()),
                Integer.parseInt(optPanel.camY.getText()),
                Integer.parseInt(optPanel.camZ.getText())));

        pyramid.getCamera().setCenter(new Point3D(
                Integer.parseInt(optPanel.camCX.getText()),
                Integer.parseInt(optPanel.camCY.getText()),
                Integer.parseInt(optPanel.camCZ.getText())));

        pyramid.getCamera().setAngle(optPanel.camSAng.getValue());

        pyramid.setLight(new Light(
                Integer.parseInt(optPanel.ligX.getText()),
                Integer.parseInt(optPanel.ligY.getText()),
                Integer.parseInt(optPanel.ligZ.getText())));

        pyramid.getLight().setColor(optPanel.colPreview.getBackground());


        xPanel.cameraPos = TransformHandler.ortX(pyramid.getCamera().getPosition());
        yPanel.cameraPos = TransformHandler.ortY(pyramid.getCamera().getPosition());
        zPanel.cameraPos = TransformHandler.ortZ(pyramid.getCamera().getPosition());

        xPanel.centerPos = TransformHandler.ortX(pyramid.getCamera().getCenter());
        yPanel.centerPos = TransformHandler.ortY(pyramid.getCamera().getCenter());
        zPanel.centerPos = TransformHandler.ortZ(pyramid.getCamera().getCenter());

        xPanel.lightPos = TransformHandler.ortX(pyramid.getLight());
        yPanel.lightPos = TransformHandler.ortY(pyramid.getLight());
        zPanel.lightPos = TransformHandler.ortZ(pyramid.getLight());

        xPanel.lightColor = optPanel.colPreview.getBackground();
        yPanel.lightColor = optPanel.colPreview.getBackground();
        zPanel.lightColor = optPanel.colPreview.getBackground();
        recalcPyramid();
            //normalizedData = normalizeData();
    }

    private void setCameraPosFromMouse(char c, Point p) {

        pyramid.getCamera().setAngle(optPanel.camSAng.getValue());
        if (c == 'x') {
            optPanel.camZ.setText(Integer.toString(p.x));
            optPanel.camY.setText(Integer.toString(p.y));
        } else if (c == 'y') {
            optPanel.camX.setText(Integer.toString(p.x));
            optPanel.camZ.setText(Integer.toString(p.y));
        } else if (c == 'z') {
            optPanel.camX.setText(Integer.toString(p.x));
            optPanel.camY.setText(Integer.toString(p.y));
        }
        pyramid.getCamera().setPosition(new Point3D(
                Integer.parseInt(optPanel.camX.getText()),
                Integer.parseInt(optPanel.camY.getText()),
                Integer.parseInt(optPanel.camZ.getText())));


        xPanel.cameraPos = TransformHandler.ortX(pyramid.getCamera().getPosition());
        yPanel.cameraPos = TransformHandler.ortY(pyramid.getCamera().getPosition());
        zPanel.cameraPos = TransformHandler.ortZ(pyramid.getCamera().getPosition());

        recalcPyramid();
    }

    private void setCameraCenterFromMouse(char c, Point p) {

        pyramid.getCamera().setAngle(optPanel.camSAng.getValue());
        if (c == 'x') {
            optPanel.camCZ.setText(Integer.toString(p.x));
            optPanel.camCY.setText(Integer.toString(p.y));
        } else if (c == 'y') {
            optPanel.camCX.setText(Integer.toString(p.x));
            optPanel.camCZ.setText(Integer.toString(p.y));
        } else if (c == 'z') {
            optPanel.camCX.setText(Integer.toString(p.x));
            optPanel.camCY.setText(Integer.toString(p.y));
        }

        pyramid.getCamera().setCenter(new Point3D(
                Integer.parseInt(optPanel.camCX.getText()),
                Integer.parseInt(optPanel.camCY.getText()),
                Integer.parseInt(optPanel.camCZ.getText())));

        xPanel.centerPos = TransformHandler.ortX(pyramid.getCamera().getCenter());
        yPanel.centerPos = TransformHandler.ortY(pyramid.getCamera().getCenter());
        zPanel.centerPos = TransformHandler.ortZ(pyramid.getCamera().getCenter());

        recalcPyramid();
    }

    private void setLightPosFromMouse(char c, Point p) {


        if (c == 'x') {
            optPanel.ligZ.setText(Integer.toString(p.x));
            optPanel.ligY.setText(Integer.toString(p.y));
        } else if (c == 'y') {
            optPanel.ligX.setText(Integer.toString(p.x));
            optPanel.ligZ.setText(Integer.toString(p.y));
        } else if (c == 'z') {
            optPanel.ligX.setText(Integer.toString(p.x));
            optPanel.ligY.setText(Integer.toString(p.y));
        }

        pyramid.setLight(new Light(
                Integer.parseInt(optPanel.ligX.getText()),
                Integer.parseInt(optPanel.ligY.getText()),
                Integer.parseInt(optPanel.ligZ.getText()),
                pyramid.getLight().getColor()));

        xPanel.lightPos = TransformHandler.ortX(pyramid.getLight());
        yPanel.lightPos = TransformHandler.ortY(pyramid.getLight());
        zPanel.lightPos = TransformHandler.ortZ(pyramid.getLight());

        recalcPyramid();
    }


    private void recalcPyramid() {
        pyramid.recalcCamera();
        worldToView = TransformHandler.worldToView(pyramid);
        currentTransform = TransformHandler.findNormTransformMatrix(pyramid, pPanel.getSize());
        for (int i = 0; i < 4; i++) {
            xPanel.camEdges[i] = TransformHandler.ortX(pyramid.edges[i]);
            yPanel.camEdges[i] = TransformHandler.ortY(pyramid.edges[i]);
            zPanel.camEdges[i] = TransformHandler.ortZ(pyramid.edges[i]);
        }

        repaintPanels();
    }

    class MyMouseAdapter extends MouseAdapter {
        final Point pp = new Point();

        @Override
        public void mouseMoved(MouseEvent e) {
            if (foundCam(e)) {
                if (!xPanel.cameraActive) {
                    cameraPosOn();
                    repaintPanels();
                }
            } else if (foundCenter(e)) {
                if (!xPanel.centerActive) {
                    centerPosOn();
                    repaintPanels();
                }
            } else if (foundLight(e)) {
                if (!xPanel.lightActive) {
                    lightOn();
                    repaintPanels();
                }
            } else {
                activeOff();
                repaintPanels();
            }
        }

        private void activeOff() {
            xPanel.cameraActive = false;
            xPanel.centerActive = false;
            xPanel.lightActive = false;
            yPanel.cameraActive = false;
            yPanel.centerActive = false;
            yPanel.lightActive = false;
            zPanel.cameraActive = false;
            zPanel.centerActive = false;
            zPanel.lightActive = false;
        }

        private void lightOn() {
            xPanel.cameraActive = false;
            xPanel.centerActive = false;
            xPanel.lightActive = true;
            yPanel.cameraActive = false;
            yPanel.centerActive = false;
            yPanel.lightActive = true;
            zPanel.cameraActive = false;
            zPanel.centerActive = false;
            zPanel.lightActive = true;
        }

        private void centerPosOn() {
            xPanel.cameraActive = false;
            xPanel.centerActive = true;
            xPanel.lightActive = false;
            yPanel.cameraActive = false;
            yPanel.centerActive = true;
            yPanel.lightActive = false;
            zPanel.cameraActive = false;
            zPanel.centerActive = true;
            zPanel.lightActive = false;
        }

        private void cameraPosOn() {
            xPanel.cameraActive = true;
            xPanel.centerActive = false;
            xPanel.lightActive = false;
            yPanel.cameraActive = true;
            yPanel.centerActive = false;
            yPanel.lightActive = false;
            zPanel.cameraActive = true;
            zPanel.centerActive = false;
            zPanel.lightActive = false;
        }


        private boolean foundCam(MouseEvent e) {
            return (Math.abs(e.getX() - xPanel.cameraPos.getX()) <= 4 && Math.abs(e.getY() - (xPanel.getHeight() - xPanel.cameraPos.getY())) <= 4) ||
                    (Math.abs(e.getX() - yPanel.cameraPos.getX()) <= 4 && Math.abs(e.getY() - (yPanel.getHeight() - yPanel.cameraPos.getY())) <= 4) ||
                    (Math.abs(e.getX() - zPanel.cameraPos.getX()) <= 4 && Math.abs(e.getY() - (zPanel.getHeight() - zPanel.cameraPos.getY())) <= 4);
        }

        private boolean foundCenter(MouseEvent e) {
            return (Math.abs(e.getX() - xPanel.centerPos.getX()) <= 4 && Math.abs(e.getY() - (xPanel.getHeight() - xPanel.centerPos.getY())) <= 4) ||
                    (Math.abs(e.getX() - yPanel.centerPos.getX()) <= 4 && Math.abs(e.getY() - (yPanel.getHeight() - yPanel.centerPos.getY())) <= 4) ||
                    (Math.abs(e.getX() - zPanel.centerPos.getX()) <= 4 && Math.abs(e.getY() - (zPanel.getHeight() - zPanel.centerPos.getY())) <= 4);
        }

        private boolean foundLight(MouseEvent e) {
            return (Math.abs(e.getX() - xPanel.lightPos.getX()) <= 4 && Math.abs(e.getY() - (xPanel.getHeight() - xPanel.lightPos.getY())) <= 4) ||
                    (Math.abs(e.getX() - yPanel.lightPos.getX()) <= 4 && Math.abs(e.getY() - (yPanel.getHeight() - yPanel.lightPos.getY())) <= 4) ||
                    (Math.abs(e.getX() - zPanel.lightPos.getX()) <= 4 && Math.abs(e.getY() - (zPanel.getHeight() - zPanel.lightPos.getY())) <= 4);
        }


        @Override
        public void mousePressed(MouseEvent e) {
            pp.x = e.getX();
            pp.y = e.getX();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point cp = new Point(e.getPoint());
            char c = 't';
            if (xPanel.cameraActive) {
                if (e.getSource() == xPanel) {
                    c = 'x';
                } else if (e.getSource() == yPanel) {
                    c = 'y';
                } else if (e.getSource() == zPanel) {
                    c = 'z';
                }
                setCameraPosFromMouse(c, new Point(e.getX(), xPanel.getHeight() - e.getY()));
            } else if (xPanel.centerActive) {
                if (e.getSource() == xPanel) {
                    c = 'x';
                } else if (e.getSource() == yPanel) {
                    c = 'y';
                } else if (e.getSource() == zPanel) {
                    c = 'z';
                }
                setCameraCenterFromMouse(c, new Point(e.getX(), xPanel.getHeight() - e.getY()));
            } else if (xPanel.lightActive) {
                if (e.getSource() == xPanel) {
                    c = 'x';
                } else if (e.getSource() == yPanel) {
                    c = 'y';
                } else if (e.getSource() == zPanel) {
                    c = 'z';
                }
                setLightPosFromMouse(c, new Point(e.getX(), xPanel.getHeight() - e.getY()));
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            ;
        }
    }

    private void repaintPanels() {
        pPanel.repaint();
        xPanel.repaint();
        yPanel.repaint();
        zPanel.repaint();
    }

}
