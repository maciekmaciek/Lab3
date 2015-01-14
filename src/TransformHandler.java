import Jama.Matrix;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-03.
 */
public final class TransformHandler {

    private static final double EPSILON = 0.5;

    private TransformHandler() {
    }

    private static Matrix toMatrix(Point3D point) {
        Matrix matrix = new Matrix(4, 1);
        matrix.set(0, 0, point.getX());
        matrix.set(1, 0, point.getY());
        matrix.set(2, 0, point.getZ());
        matrix.set(3, 0, 1);
        return matrix;
    }

    //Punkt należy zrzutować na oś.
    public static Point ortX(Point3D position) {    //y pionowo, z poziomo
        return new Point((int) (position.getZ()), (int) (position.getY()));
    }

    public static Point ortY(Point3D position) {    //z pionowo, x poziomo
        return new Point((int) (position.getX()), (int) (position.getZ()));
    }

    public static Point ortZ(Point3D position) {    //y pionowo, x poziomo
        return new Point((int) (position.getX()), (int) (position.getY()));
    }

    public static Point persp(Point3D position) {    //y pionowo, x poziomo
        return new Point((int) (position.getX()), (int) (position.getY()));
    }


    /*public static ArrayList<Point3D> arrayToPerspective(ArrayList<Point3D> points, Pyramid pyramid) {
        Matrix transform = findNormTransformMatrix(pyramid);
        ArrayList<Point3D> result = new ArrayList<>();
        for (Point3D p3D : points) {
            result.add(applyTransformToPoint(p3D, transform,pyramid.camera));
        }
        return result;
    }*/

    public static Point3D applyTransformToPoint(Point3D p3D, Matrix transform){
        Matrix pointM = toMatrix(p3D);
        pointM = transform.times(pointM);
        if(p3D.getClass() == Light.class) {
            Light p3D2 = (Light) p3D;
            return new Light((int)(pointM.get(0,0)/pointM.get(3,0)),
                    (int)(pointM.get(1,0)/pointM.get(3,0)),
                    (int)(pointM.get(2,0)/pointM.get(3,0)),
                    p3D2.getColor());
        }
        else if(p3D.getClass() == ColorPoint.class) {
            ColorPoint p3D1 = (ColorPoint) p3D;
            return new ColorPoint((int)(pointM.get(0,0)/pointM.get(3,0)),
                    (int)(pointM.get(1,0)/pointM.get(3,0)),
                    (int)(pointM.get(2,0)/pointM.get(3,0)),
                    p3D1.color,
                    p3D1.kd,
                    p3D1.ks,
                    p3D1.g
                    );
        }
        else return new Point3D((int)(pointM.get(0,0)/pointM.get(3,0)),
                    (int)(pointM.get(1,0)/pointM.get(3,0)),
                    (int)(pointM.get(2,0)/pointM.get(3,0)));
    }


   /* public static ColorPoint+ applyTransformToPoint(ColorPoint p3D, Matrix transform) {
        Matrix pointM = toMatrix(p3D);
        pointM = transform.times(pointM);

        return new ColorPoint(
                (int)(pointM.get(0,0)/pointM.get(3,0)),
                (int)(pointM.get(1,0)/pointM.get(3,0)),
                (int)(pointM.get(2,0)/pointM.get(3,0)),
                p3D.color
                );
    }

*/
    private static Matrix rotX(Matrix m, double angle){
        double[][] translation = {
                {1, 0, 0, 0},
                {0, Math.cos(angle), -Math.sin(angle), 0},
                {0, Math.sin(angle), Math.cos(angle), 0},
                {0, 0, 0, 1}
        };
        Matrix step = new Matrix(translation);
        return step.times(m);

    }

    private static Matrix rotY(Matrix m,double angle){
        double[][] translation = {
                {Math.cos(angle), 0, Math.sin(angle), 0},
                {0, 1, 0, 1},
                {-Math.sin(angle), 0, Math.cos(angle), 0},
                {0, 0, 0, 1}
        };
        Matrix step = new Matrix(translation);
        return step.times(m);

    }

    private static Matrix scale(Matrix m, double x, double y, double z){
        double[][] translation = {
                {x, 0, 0, 0},
                {0, y, 0, 0},
                {0, 0, z, 0},
                {0, 0, 0, 1}
        };
        Matrix step = new Matrix(translation);
        return step.times(m);

    }

    private static Matrix translate(Matrix m, double x, double y, double z){
        double[][] translation = {
                {1, 0, 0, x},
                {0, 1, 0, y},
                {0, 0, 1, z},
                {0, 0, 0, 1}
        };
        Matrix step = new Matrix(translation);
        return step.times(m);

    }

    private static Matrix perspective(Matrix m, double d){
        double[][] translation = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 1/d, 1}
        };
        Matrix step = new Matrix(translation);
        return step.times(m);

    }

    public static Matrix worldToView(Pyramid pyramid){
        Camera camera = pyramid.camera;
        double angle;
        Matrix tempRes = toMatrix(camera.getPosition());
        System.out.println(tempRes.get(0,0)/tempRes.get(3,0) + ", " + tempRes.get(1,0)/tempRes.get(3,0) + ", " + tempRes.get(2,0)/tempRes.get(3,0));
        double[][] onesArr = {
                {1.0, 0.0, 0.0, 0.0},
                {0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 1.0},
        };
        Matrix res = new Matrix(onesArr);
//przesunięcie środka do 0.0


        res = translate(res,
                -camera.getCenter().getX(),
                -camera.getCenter().getY(),
                -camera.getCenter().getZ());

        tempRes = translate(tempRes,
                -camera.getCenter().getX(),
                -camera.getCenter().getY(),
                -camera.getCenter().getZ());
        System.out.println(tempRes.get(0,0)/tempRes.get(3,0) + ", " + tempRes.get(1,0)/tempRes.get(3,0) + ", " + tempRes.get(2,0)/tempRes.get(3,0));

// obrót wokół OY

        angle = Math.PI - Math.atan2(tempRes.get(0, 0), tempRes.get(2, 0));

        res = rotY(res, angle);
        tempRes = res.times(toMatrix(camera.getPosition()));

        System.out.println(tempRes.get(0,0)/tempRes.get(3,0) + ", " + tempRes.get(1,0)/tempRes.get(3,0) + ", " + tempRes.get(2,0)/tempRes.get(3,0));
// obrót wokół OX

        angle = -Math.PI / 2 - Math.atan2(tempRes.get(2, 0), tempRes.get(1, 0));

        res = rotX(res, angle);
        tempRes = res.times(toMatrix(camera.getPosition()));
        System.out.println(tempRes.get(0,0)/tempRes.get(3,0) + ", " + tempRes.get(1,0)/tempRes.get(3,0) + ", " + tempRes.get(2,0)/tempRes.get(3,0));

        return res;

    }
    public static Matrix viewToFlat(Pyramid pyramid, Dimension panelDim) {
        double[][] onesArr = {
                {1.0, 0.0, 0.0, 0.0},
                {0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 1.0},
        };
        Matrix res = new Matrix(onesArr);

// rzutowanie perspektywiczne
        double d = pyramid.getDistVec().length();

        res = perspective(res, d);

// skalowanie do obrazu
        double sx = panelDim.getWidth()/pyramid.getWidth();
        double sy = panelDim.getHeight()/pyramid.getHeight();

        res = scale(res, sx, sy, 1);

// przesunięcie do 0.0*/
        double tx = pyramid.getWidth();
        double ty = pyramid.getHeight();

        res = translate(res, tx, ty, 0);

        return res;

    }

    public static Matrix findNormTransformMatrix(Pyramid pyramid, Dimension panelDim) {
        Matrix res = worldToView(pyramid);
        res = viewToFlat(pyramid,panelDim).times(res);
        return res;
    }

    public static Color phongLight(Light l, ColorPoint cp, Point3D camPos){    //W UKŁADZIE OBSERWATORA
        HashMap<Integer,Integer> konik;
        Vec3d lm = new Vec3d(l.getX() - cp.getX(),l.getY() - cp.getY(), l.getZ() - cp.getZ());
        lm.normalize();
        Vec3d norm = cp.normal;
        Vec3d vw = new Vec3d(camPos.getX() - cp.getX(), camPos.getY() - cp.getY(), camPos.getZ() - cp.getZ());
        vw.normalize();
        vw.mul(0.5);
        vw.add(lm);
        double result = cp.kd*norm.dot(lm) + cp.ks*Math.max(Math.pow(norm.dot(vw), cp.g), 0);
        //Ka + Kd * (N dot L) + Ks * (N dot ( L + V / 2))^n
        Color c = new Color(
                phongLightBeam(
                        l.getColor().getRed(),
                        cp.color.getRed(),
                        result),
                phongLightBeam(
                        l.getColor().getGreen(),
                        cp.color.getGreen(),
                        result),
                phongLightBeam(
                        l.getColor().getBlue(),
                        cp.color.getBlue(),
                        result));
        return c;
    }
    public static double[] findBarycentric(int px, int py, int x1, int y1, int x2, int y2, int x3, int y3){ // W UKŁADZIE OBSERWATORA
        double a, b, c;

        b = (double)(((x1 - x3) * (py - y3) - px + x3))/
                    ((y2 - y3) * (x1-x3));

        a = ((px - b * (x2 - x3) - x3))/
                        (x1 - x3);

        c = 1 - a - b;
        double[] lambdas = {a, b, c};
        return lambdas;
    }

    public static int phongLightBeam(int lightC, int vertC, double res){
        return (int)(vertC*(res + (double)(lightC/255))); //lepiej
    }
}