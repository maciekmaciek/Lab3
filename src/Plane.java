import javafx.geometry.Point3D;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-05.
 */
public class Plane {

    public Plane(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    public Plane(Point3D p1,Point3D p2, Point3D p3){

    }

    public Plane() {
    }

    ;

    private double a;
    private double b;
    private double c;
    private double d;

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

}
