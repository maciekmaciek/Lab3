import Jama.Matrix;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import javafx.util.Pair;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-07.
 */
public class Triangle implements Comparable{

    ColorPoint a;
    ColorPoint b;
    ColorPoint c;
    Vec3d normal;
    ArrayList<ColorPoint> sortedTransformed;

    public Triangle(ColorPoint a, ColorPoint b, ColorPoint c) {
        this.a = a;
        this.b = b;
        this.c = c;
        calcNormal();
        sortedTransformed = new ArrayList<>();
    }

    void transform(Matrix transform){
        sortedTransformed.add((ColorPoint)TransformHandler.applyTransformToPoint(a, transform));
        sortedTransformed.add((ColorPoint)TransformHandler.applyTransformToPoint(b, transform));
        sortedTransformed.add((ColorPoint)TransformHandler.applyTransformToPoint(c, transform));
        Collections.sort(sortedTransformed);
    }

    void calcNormal(){
        normal = new Vec3d();
        Vec3d v1 = new Vec3d(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
        Vec3d v2 = new Vec3d(c.getX() - a.getX(), c.getY() - a.getY(), c.getZ() - a.getZ());
        normal.cross(v1, v2);
        normal.normalize();

    }

    public Triangle[] split(){
        Collections.sort(sortedTransformed);
        double x1 = sortedTransformed.get(0).getX();
        double y1 = sortedTransformed.get(0).getY();
        double z1 = sortedTransformed.get(0).getZ();
        double r1 = sortedTransformed.get(0).color.getRed();
        double g1 = sortedTransformed.get(0).color.getGreen();
        double b1 = sortedTransformed.get(0).color.getBlue();
        double x2 = sortedTransformed.get(1).getX();
        double y2 = sortedTransformed.get(1).getY();
        double z2 = sortedTransformed.get(1).getZ();
        double r2 = sortedTransformed.get(1).color.getRed();
        double g2 = sortedTransformed.get(1).color.getGreen();
        double b2 = sortedTransformed.get(1).color.getBlue();
        double x3 = sortedTransformed.get(2).getX();
        double y3 = sortedTransformed.get(2).getY();
        double z3 = sortedTransformed.get(2).getZ();
        double r3 = sortedTransformed.get(2).color.getRed();
        double g3 = sortedTransformed.get(2).color.getGreen();
        double b3 = sortedTransformed.get(2).color.getBlue();
        if (y1 == y2 && y2 == y3) {
            Triangle[] t = new Triangle[1];
            t[1] = this;
            return t;
        }
        double y4 = y2;
        double x4 =((y4 - y1) * ((x1 - x3) / (y1 - y3)) + x1);
        double z4 =((y4 - y1) * ((z1 - z3) / (y1 - y3)) + z1);
        double r4 =((y4 - y1) * ((r1 - r3) / (y1 - y3)) + r1);
        double g4 =((y4 - y1) * ((g1 - g3) / (y1 - y3)) + g1);
        double b4 =((y4 - y1) * ((b1 - b3) / (y1 - y3)) + b1);
//color interpolation
        ColorPoint v1 = sortedTransformed.get(0);
        ColorPoint v2 = sortedTransformed.get(1);
        ColorPoint v3 = sortedTransformed.get(2);
        ColorPoint v4 = new ColorPoint(x4,y4, z4, new Color((int)r4, (int)g4, (int)b4));
        Triangle t1 = new Triangle(v1,v2,v4);
        Triangle t2 = new Triangle(v2, v3, v4);
        Collections.sort(t1.sortedTransformed);
        Collections.sort(t2.sortedTransformed);
        Triangle[] t = new Triangle[2];
        t[0] = t1;
        t[1] = t2;
        return t;
    }

    @Override
    public int compareTo(Object o) {
        Triangle toCompare = (Triangle) o;
        if(toCompare.sortedTransformed.isEmpty() ||
                sortedTransformed.isEmpty())
            return 0;
        else {
            double tcZ = 0;
            double tz = 0;
            for(int i = 0; i< sortedTransformed.size(); i++){
                tz+= sortedTransformed.get(i).getZ();
                tcZ+= toCompare.sortedTransformed.get(i).getZ();
            }
            if(tz > tcZ)
                return 1;
            else if(tz == tcZ)
                return 0;
            else return -1;
        }
    }
}
