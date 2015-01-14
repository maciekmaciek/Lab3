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

    public ColorPoint(int i, int i1, int i2, Color color, double kd, double ks, int g) {
        super(i, i1, i2);
        this.color = color;
        this.kd = kd;
        this.ks = ks;
        this.g = g;
    }

    public void calcNormal() {
        Vec3d temp = new Vec3d();
        for (Triangle t : triangles) {
            temp.add(t.normal);
        }
        normal.add(temp);
        normal.mul(1 / normal.length());
    }


    @Override
    public int compareTo(Object o) {
        ColorPoint cp = (ColorPoint) o;

        if (cp.getY() < getY())
            return -1;
        else if (cp.getY() == getY())
            return 0;
        else return 1;
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
