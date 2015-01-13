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
    ArrayList<ColorPoint> points;
    ArrayList<Triangle> triangles;

    public DrawnData(Light light, Camera camera, ArrayList<ColorPoint> points, ArrayList<Triangle> triangles) {
        this.light = light;
        this.camera = camera;
        this.points = points;
        this.triangles = triangles;

        addTrianglesToPoints();
        countNormals();
    }

    private void countNormals() {
        for(ColorPoint cp: points){
            cp.calcNormal();
        }
    }

    private void addTrianglesToPoints() {
        for(ColorPoint cp: points){
            for(Triangle t:triangles){
                if(t.a == cp || t.b == cp || t.c == cp){
                    cp.triangles.add(t);
                }
            }
        }
    }


}
