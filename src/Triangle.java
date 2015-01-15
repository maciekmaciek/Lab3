import Jama.Matrix;
import com.sun.javafx.geom.Vec3d;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-07.
 */
public class Triangle implements Comparable {

    ColorPoint a;
    ColorPoint b;
    ColorPoint c;
    Vec3d normal;
    ArrayList<ColorPoint> sortedTransformed;
    ArrayList<ColorPoint> sorted;

    public Triangle(ColorPoint a, ColorPoint b, ColorPoint c) {
        this.a = a;
        this.b = b;
        this.c = c;
        calcNormal();
        sorted = new ArrayList<>();
        sortedTransformed = new ArrayList<>();
    }

    void transform(Matrix transform) {
        sortedTransformed.clear();
        sortedTransformed.add((ColorPoint) TransformHandler.applyTransformToPoint(a, transform));
        sortedTransformed.add((ColorPoint) TransformHandler.applyTransformToPoint(b, transform));
        sortedTransformed.add((ColorPoint) TransformHandler.applyTransformToPoint(c, transform));
        sort();
    }


    void sort() { //
        sorted.clear();
        sorted.add(a);
        sorted.add(b);
        sorted.add(c);

        for (int i = 0; i < sorted.size(); i++) {
            for (int j = 0; j < (sorted.size() - 1); j++) {
                int comp = sortedTransformed.get(j).compareTo(sortedTransformed.get(j + 1));
                if (comp < 0) {
                    ColorPoint tst;
                    ColorPoint ts;
                    tst = sortedTransformed.get(j + 1);
                    ts = sorted.get(j + 1);
                    sortedTransformed.set(j + 1, sortedTransformed.get(j));
                    sorted.set(j + 1, sorted.get(j));
                    sortedTransformed.set(j, tst);
                    sorted.set(j, ts);
                }
            }
        }
    }
    void calcNormal() {
        normal = new Vec3d();
        Vec3d v1 = new Vec3d(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
        Vec3d v2 = new Vec3d(c.getX() - a.getX(), c.getY() - a.getY(), c.getZ() - a.getZ());
        normal.cross(v1, v2);
        normal.normalize();

    }

    public Triangle[] split() { //UWAGA ZWRACA 2D
        sort();
        Vec3d norm1 = sortedTransformed.get(0).normal;
        Vec3d norm2 = sortedTransformed.get(0).normal;
        Vec3d norm3 = sortedTransformed.get(0).normal;
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
        Triangle[] t = new Triangle[2];
        ColorPoint v1 = sortedTransformed.get(0);
        ColorPoint v2 = sortedTransformed.get(1);
        ColorPoint v3 = sortedTransformed.get(2);

        if (y1 == y2) {
            t[1] = new Triangle(v1, v2, v3);
            t[1].sortedTransformed.add(v1);       //trik do sortowania
            t[1].sortedTransformed.add(v2);
            t[1].sortedTransformed.add(v3);
            t[1].sort();
            return t;
        } else if (y2 == y3) {
            t[0] = new Triangle(v1, v2, v3);
            t[0] = new Triangle(v1, v2, v3);
            t[0].sortedTransformed.add(v1);       //trik do sortowania
            t[0].sortedTransformed.add(v2);
            t[0].sortedTransformed.add(v3);
            t[0].sort();
            return t;
        }
        double y4 = y2;
        double x4 = ((y4 - y1) * ((x1 - x3) / (y1 - y3)) + x1);
        double z4 = ((y4 - y1) * ((z1 - z3) / (y1 - y3)) + z1);
        double r4 = ((y4 - y1) * ((r1 - r3) / (y1 - y3)) + r1);
        double g4 = ((y4 - y1) * ((g1 - g3) / (y1 - y3)) + g1);
        double b4 = ((y4 - y1) * ((b1 - b3) / (y1 - y3)) + b1);
        //Vec3d normal4 = new Vec3d();
        //normal4.add(norm1);         //temp czy w ogóle zadziała!
//color interpolation

        ColorPoint v4 = new ColorPoint((int) x4, (int) y4, (int) z4, new Color((int) r4, (int) g4, (int) b4), v1.kd, v1.ks, v1.g);
        //v4.normal = normal4;
        Triangle t1 = new Triangle(v1, v2, v4);
        t1.sortedTransformed.add(v1);       //trik do sortowania
        t1.sortedTransformed.add(v2);
        t1.sortedTransformed.add(v4);
        Triangle t2 = new Triangle(v2, v4, v3);
        t2.sortedTransformed.add(v2);       //trik do sortowania
        t2.sortedTransformed.add(v4);
        t2.sortedTransformed.add(v3);
        t1.sort();
        t2.sort();
        //Collections.sort(t1.sortedTransformed);
        //Collections.sort(t2.sortedTransformed);
        t[0] = t1;
        t[1] = t2;
        return t;
    }

    public double getSumZ() {
        return a.getZ()+b.getZ()+c.getZ();
    }

    @Override
    public int compareTo(Object o) {
        Triangle toCompare = (Triangle) o;

        double tcZ = toCompare.getSumZ();
        double tz = getSumZ();

        if (tz > tcZ)
            return 1;
        else if (tz == tcZ)
            return 0;
        else return -1;
    }
}
