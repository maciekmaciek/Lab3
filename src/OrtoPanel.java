import javax.swing.*;
import java.awt.*;

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
    Point[] edges;
    boolean cameraActive;
    boolean centerActive;
    boolean lightActive;


    public OrtoPanel(String title, Gui gui) {

        this.gui = gui;
        this.title = title;
        cameraPos = new Point();
        centerPos = new Point();
        lightPos = new Point();
        edges = new Point[4];
        for (int i = 0; i < 4; i++)
            edges[i] = new Point();
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
        if (edges != null) {
            for (int i = 0; i < 3; i++) {
                g2d.drawLine(edges[i].x, getHeight() - edges[i].y, edges[i + 1].x, getHeight() - edges[i + 1].y);
                g2d.drawLine(edges[i].x, getHeight() - edges[i].y, cameraPos.x, getHeight() - cameraPos.y);
            }
            g2d.drawLine(edges[3].x, getHeight() - edges[3].y, edges[0].x, getHeight() - edges[0].y);
            g2d.drawLine(edges[3].x, getHeight() - edges[3].y, cameraPos.x, getHeight() - cameraPos.y);
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

    }
}
