import javafx.geometry.Point3D;

import java.awt.*;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-10.
 */
public class ColorPoint extends Point3D {
    Color color;
    double kd;  //rozpraszanie
    double ks;  //polyskliwosc
    double g;   //polyskliwosc g
    public ColorPoint(double x, double y, double z, Color c){
        super(x,y,z);
        c = color;
    }
}
