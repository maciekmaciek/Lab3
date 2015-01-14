import javax.swing.*;
import java.awt.*;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2014-12-31.
 */
public class PerspectivePanel extends JPanel {
    private Gui gui;

    public PerspectivePanel(Gui gui) {
        this.gui = gui;
        setOpaque(true);
        setBackground(Color.DARK_GRAY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawFigures(g2d);
        g2d.setPaint(Color.red);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.drawString("Real", 10, 25);
    }

    private void drawFigures(Graphics2D g2d) {
        if(!(gui.renderer == null) && !(gui.normalizedData == null))
            g2d.drawImage(gui.renderer.drawPersp(), null, null);

        g2d.setPaint(Color.white);
        g2d.setStroke(new BasicStroke(1));
        if (gui.CURRENT_FILE_PATH != null) {
            for (Triangle t : gui.drawnData.triangles) {
                int ax = (int) (TransformHandler.applyTransformToPoint(t.a, gui.currentTransform).getX());
                int ay = getHeight() - (int) (TransformHandler.applyTransformToPoint(t.a, gui.currentTransform).getY());
                int bx = (int) (TransformHandler.applyTransformToPoint(t.b, gui.currentTransform).getX());
                int by = getHeight() - (int) (TransformHandler.applyTransformToPoint(t.b, gui.currentTransform).getY());
                int cx = (int) (TransformHandler.applyTransformToPoint(t.c, gui.currentTransform).getX());
                int cy = getHeight() - (int) (TransformHandler.applyTransformToPoint(t.c, gui.currentTransform).getY());
                g2d.drawLine(
                        ax,
                        ay,
                        bx,
                        by);

                g2d.drawLine(
                        ax,
                        ay,
                        cx,
                        cy);

                g2d.drawLine(
                        bx,
                        by,
                        cx,
                        cy);

            }
        }
    }

}
