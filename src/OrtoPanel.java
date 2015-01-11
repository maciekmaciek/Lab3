import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2014-12-31.
 */
public class OrtoPanel extends JPanel {
    String title;
    Gui gui;
    Point cameraPos;
    Point centerPos;
    Point lightPos;
    Color lightColor;
    Point[] camEdges;
    BufferedImage currentImg;
   // Point
    boolean cameraActive;
    boolean centerActive;
    boolean lightActive;


    public OrtoPanel(String title, Gui gui) {

        this.gui = gui;
        this.title = title;
        cameraPos = new Point();
        centerPos = new Point();
        lightPos = new Point();
        camEdges = new Point[4];
        for (int i = 0; i < 4; i++)
            camEdges[i] = new Point();
        setOpaque(true);
        setBackground(Color.DARK_GRAY);
        addMouseMotionListener(gui.myMouseAdapter);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        drawFigures(g2d);
        drawPoints(g2d);
        drawLines(g2d);


        g2d.setPaint(Color.red);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        g2d.drawString(title, 15, 35);
    }

    private void drawLines(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.white);
        if (camEdges != null) {
            for (int i = 0; i < 3; i++) {
                g2d.drawLine(camEdges[i].x, getHeight() - camEdges[i].y, camEdges[i + 1].x, getHeight() - camEdges[i + 1].y);
                g2d.drawLine(camEdges[i].x, getHeight() - camEdges[i].y, cameraPos.x, getHeight() - cameraPos.y);
            }
            g2d.drawLine(camEdges[3].x, getHeight() - camEdges[3].y, camEdges[0].x, getHeight() - camEdges[0].y);
            g2d.drawLine(camEdges[3].x, getHeight() - camEdges[3].y, cameraPos.x, getHeight() - cameraPos.y);
        }
    }

    private void drawPoints(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(3));
        if (cameraPos != null) {
            if (cameraActive) {
                g2d.setPaint(Color.red);
            } else {
                g2d.setPaint(new Color(255, 0, 0, 70));
            }
            g2d.fillOval(cameraPos.x - 7, getHeight() - cameraPos.y - 7, 14, 14);
        }

        if (centerPos != null) {
            if (centerActive) {
                g2d.setPaint(Color.green);
            } else {
                g2d.setPaint(new Color(0, 255, 0, 70));
            }
            g2d.drawRect(centerPos.x - 7, getHeight() - centerPos.y - 7, 14, 14);
        }

        if (lightPos != null) {
            g2d.setPaint(lightColor);
            if (!lightActive) {
                g2d.fillOval(lightPos.x - 7, getHeight() - lightPos.y - 7, 14, 14);
            } else {
                g2d.fillOval(lightPos.x - 10, getHeight() - lightPos.y - 10, 20, 20);
            }
        }
    }

    private void drawFigures(Graphics2D g2d) {
        //g2d.drawImage(currentImg, null,0,0);
        g2d.setPaint(Color.white);
        g2d.setStroke(new BasicStroke(1));
        if(gui.CURRENT_FILE_PATH!=null) {
            if (title.equals("X")) {
                for (Triangle t : gui.drawnData.triangles) {
                    g2d.drawLine(
                            TransformHandler.ortX(t.a).x,
                            TransformHandler.ortX(t.a).y,
                            TransformHandler.ortX(t.b).x,
                            TransformHandler.ortX(t.b).y);

                    g2d.drawLine(
                            TransformHandler.ortX(t.a).x,
                            TransformHandler.ortX(t.a).y,
                            TransformHandler.ortX(t.c).x,
                            TransformHandler.ortX(t.c).y);

                    g2d.drawLine(
                            TransformHandler.ortX(t.c).x,
                            TransformHandler.ortX(t.c).y,
                            TransformHandler.ortX(t.b).x,
                            TransformHandler.ortX(t.b).y);
                }

            } else if (title.equals("Y")) {
                for (Triangle t : gui.drawnData.triangles) {
                    g2d.drawLine(
                            TransformHandler.ortY(t.a).x,
                            getHeight() - TransformHandler.ortY(t.a).y,
                            TransformHandler.ortY(t.b).x,
                            getHeight() - TransformHandler.ortY(t.b).y);

                    g2d.drawLine(
                            TransformHandler.ortY(t.a).x,
                            getHeight() - TransformHandler.ortY(t.a).y,
                            TransformHandler.ortY(t.c).x,
                            getHeight() - TransformHandler.ortY(t.c).y);

                    g2d.drawLine(
                            TransformHandler.ortY(t.c).x,
                            getHeight() - TransformHandler.ortY(t.c).y,
                            TransformHandler.ortY(t.b).x,
                            getHeight() - TransformHandler.ortY(t.b).y);
                }
             } else {
                for (Triangle t : gui.drawnData.triangles) {
                    g2d.drawLine(
                            TransformHandler.ortZ(t.a).x,
                            getHeight() - TransformHandler.ortZ(t.a).y,
                            TransformHandler.ortZ(t.b).x,
                            getHeight() - TransformHandler.ortZ(t.b).y);

                    g2d.drawLine(
                            TransformHandler.ortZ(t.a).x,
                            getHeight() - TransformHandler.ortZ(t.a).y,
                            TransformHandler.ortZ(t.c).x,
                            getHeight() - TransformHandler.ortZ(t.c).y);

                    g2d.drawLine(
                            TransformHandler.ortZ(t.c).x,
                            getHeight() - TransformHandler.ortZ(t.c).y,
                            TransformHandler.ortZ(t.b).x,
                            getHeight() - TransformHandler.ortZ(t.b).y);
                }
            }
        }
    }
}
