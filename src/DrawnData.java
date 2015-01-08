import javafx.geometry.Point3D;

import java.util.ArrayList;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-03.
 */
public class DrawnData {
    Light light;
    Camera camera;
    ArrayList<Point3D> points;
    ArrayList<Triangle> triangles;

    public DrawnData(Light light, Camera camera, ArrayList<Point3D> points, ArrayList<Triangle> triangles) {
        this.light = light;
        this.camera = camera;
        this.points = points;
        this.triangles = triangles;
    }


}
