import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-10.
 */
public class ColorPoint extends Point3D implements Comparable{
    Color color;
    double kd;  //rozpraszanie
    double ks;  //polyskliwosc
    int g;   //polyskliwosc g
    Vec3d normal;
    ArrayList<Triangle> triangles;
    public ColorPoint(double x, double y, double z, Color c){
        super(x,y,z);
        c = color;
        normal = new Vec3d();
        triangles = new ArrayList<>();
    }

    public void calcNormal(){
        Vec3d temp = new Vec3d();
        for(Triangle t : triangles){
            temp.add(t.normal);
        }
        normal.add(temp);
        normal.mul(1/normal.length());
    }


    @Override
    public int compareTo(Object o) {
        ColorPoint cp = (ColorPoint) o;

        if(cp.getY() < getY())
            return -1;
        else if(cp.getY() == getY())
            return 0;
        else return 1;
    }
}
