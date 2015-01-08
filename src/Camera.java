import javafx.geometry.Point3D;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-03.
 */
public class Camera {
    public Camera() {
        position = new Point3D(0, 0, 0);
        center = new Point3D(0, 0, 0);
    }

    private Point3D position;
    private Point3D center;
    private int angle;


    public Point3D getCenter() {
        return center;
    }

    public void setCenter(Point3D center) {
        this.center = center;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        if (angle < 1)
            this.angle = 1;
        else if (angle > 89)
            this.angle = 89;
        else
            this.angle = angle;
    }

    public Point3D getPosition() {
        return position;
    }

    public void setPosition(Point3D position) {
        this.position = position;
    }
}
