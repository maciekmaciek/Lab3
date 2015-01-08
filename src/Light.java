import javafx.geometry.Point3D;

import java.awt.*;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-03.
 */
public class Light {
    public Light() {
        position = new Point3D(0, 0, 0);
    }

    private Point3D position;
    private Color color;

    public Point3D getPosition() {
        return position;
    }

    public void setPosition(Point3D position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
