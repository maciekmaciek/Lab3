import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-10.
 */
public class ColorPoint extends Point3D implements Comparable {
    Color color;
    double kd;  //rozpraszanie
    double ks;  //polyskliwosc
    int g;   //polyskliwosc g
    Vec3d normal;
    ArrayList<Triangle> triangles;

    public ColorPoint(double x, double y, double z, Color c) {
        super(x, y, z);
        color = c;
        normal = new Vec3d();
        triangles = new ArrayList<>();
    }

    public ColorPoint(int x, int y, int z, Color color, double kd, double ks, int g) {
        super(x, y, z);
        this.color = color;
        this.kd = kd;
        this.ks = ks;
        this.g = g;

        normal = new Vec3d();
        triangles = new ArrayList<>();
    }

    public void calcNormal() {
        Vec3d temp = new Vec3d();
        double length = 0;
        for (Triangle t : triangles) {
            temp.add(t.normal);
            length += t.normal.length();
        }
        normal.add(temp);
        normal.mul(1 / length);
        normal.normalize();
    }


    @Override
    public int compareTo(Object o) {
        ColorPoint cp = (ColorPoint) o;

        if (Double.compare(cp.getY(), getY()) > 0) {
            return 1;
        } else if (Double.compare(cp.getY(), getY()) == 0) {
            if (Double.compare(cp.getX(), getX()) > 0) {
                return 1;
            } else if (Double.compare(cp.getX(), getX()) == 0) {
                return 0;
            } else {
                return -1;
            }
        } else return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ColorPoint) {
            ColorPoint c = (ColorPoint) o;
            return c.getX() == getX() && c.getY() == getY() && c.getZ() == getZ();
        }
        return false;
    }
}
