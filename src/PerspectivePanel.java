import javax.swing.*;
import java.awt.*;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2014-12-31.
 */
public class PerspectivePanel extends JPanel {
    public PerspectivePanel() {
        setOpaque(true);
        setBackground(Color.DARK_GRAY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.red);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.drawString("Real", 10, 25);
    }

}
