import javafx.geometry.Point3D;

import java.awt.*;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-03.
 */
public class Light extends Point3D {
    public Light() {
        super(0, 0, 0);
    }

    private Color color;

    public Light(double v, double v1, double v2) {
        super(v, v1, v2);
    }

    public Light(int i, int i1, int i2, Color color) {
        super(i, i1, i2);
        this.color = color;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
