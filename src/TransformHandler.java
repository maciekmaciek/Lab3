import Jama.Matrix;
import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.awt.*;
import java.util.HashMap;


/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-03.
 */
public final class TransformHandler {

    private static final double EPSILON = 0.00001;

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

    public static Point3D applyTransformToPoint(Point3D p3D, Matrix transform) {
        Matrix pointM = toMatrix(p3D);
        pointM = transform.times(pointM);
        if (p3D.getClass() == Light.class) {
            Light p3D2 = (Light) p3D;
            return new Light((int) (pointM.get(0, 0) / pointM.get(3, 0)),
                    (int) (pointM.get(1, 0) / pointM.get(3, 0)),
                    (int) (pointM.get(2, 0) / pointM.get(3, 0)),
                    p3D2.getColor());
        } else if (p3D.getClass() == ColorPoint.class) {
            ColorPoint p3D1 = (ColorPoint) p3D;
            return new ColorPoint((int) (pointM.get(0, 0) / pointM.get(3, 0)),
                    (int) (pointM.get(1, 0) / pointM.get(3, 0)),
                    (int) (pointM.get(2, 0) / pointM.get(3, 0)),
                    p3D1.color,
                    p3D1.kd,
                    p3D1.ks,
                    p3D1.g
            );
        } else return new Point3D((int) (pointM.get(0, 0) / pointM.get(3, 0)),
                (int) (pointM.get(1, 0) / pointM.get(3, 0)),
                (int) (pointM.get(2, 0) / pointM.get(3, 0)));
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
    private static Matrix rotX(Matrix m, double angle) {
        double[][] translation = {
                {1, 0, 0, 0},
                {0, Math.cos(angle), -Math.sin(angle), 0},
                {0, Math.sin(angle), Math.cos(angle), 0},
                {0, 0, 0, 1}
        };
        Matrix step = new Matrix(translation);
        return step.times(m);

    }

    private static Matrix rotY(Matrix m, double angle) {
        double[][] translation = {
                {Math.cos(angle), 0, Math.sin(angle), 0},
                {0, 1, 0, 1},
                {-Math.sin(angle), 0, Math.cos(angle), 0},
                {0, 0, 0, 1}
        };
        Matrix step = new Matrix(translation);
        return step.times(m);

    }

    private static Matrix scale(Matrix m, double x, double y, double z) {
        double[][] translation = {
                {x, 0, 0, 0},
                {0, y, 0, 0},
                {0, 0, z, 0},
                {0, 0, 0, 1}
        };
        Matrix step = new Matrix(translation);
        return step.times(m);

    }

    private static Matrix translate(Matrix m, double x, double y, double z) {
        double[][] translation = {
                {1, 0, 0, x},
                {0, 1, 0, y},
                {0, 0, 1, z},
                {0, 0, 0, 1}
        };
        Matrix step = new Matrix(translation);
        return step.times(m);

    }

    private static Matrix perspective(Matrix m, double d) {
        double[][] translation = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 1 / d, 1}
        };
        Matrix step = new Matrix(translation);
        return step.times(m);

    }

    public static Matrix worldToView(Pyramid pyramid) {
        Camera camera = pyramid.camera;
        double angle;
        Matrix tempRes = toMatrix(camera.getPosition());
        System.out.println(tempRes.get(0, 0) / tempRes.get(3, 0) + ", " + tempRes.get(1, 0) / tempRes.get(3, 0) + ", " + tempRes.get(2, 0) / tempRes.get(3, 0));
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
        System.out.println(tempRes.get(0, 0) / tempRes.get(3, 0) + ", " + tempRes.get(1, 0) / tempRes.get(3, 0) + ", " + tempRes.get(2, 0) / tempRes.get(3, 0));

// obrót wokół OY

        angle = Math.PI - Math.atan2(tempRes.get(0, 0), tempRes.get(2, 0));

        res = rotY(res, angle);
        tempRes = res.times(toMatrix(camera.getPosition()));

        System.out.println(tempRes.get(0, 0) / tempRes.get(3, 0) + ", " + tempRes.get(1, 0) / tempRes.get(3, 0) + ", " + tempRes.get(2, 0) / tempRes.get(3, 0));
// obrót wokół OX

        angle = -Math.PI / 2 - Math.atan2(tempRes.get(2, 0), tempRes.get(1, 0));

        res = rotX(res, angle);
        tempRes = res.times(toMatrix(camera.getPosition()));
        System.out.println(tempRes.get(0, 0) / tempRes.get(3, 0) + ", " + tempRes.get(1, 0) / tempRes.get(3, 0) + ", " + tempRes.get(2, 0) / tempRes.get(3, 0));

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
        double sx = panelDim.getWidth() / pyramid.getWidth();
        double sy = panelDim.getHeight() / pyramid.getHeight();

        res = scale(res, sx, sy, 1);

// przesunięcie do 0.0*/
        double tx = pyramid.getWidth() * sx / 2;
        double ty = pyramid.getHeight() * sy / 2;

        res = translate(res, tx, ty, 0);

        return res;

    }

    public static Matrix findNormTransformMatrix(Pyramid pyramid, Dimension panelDim) {
        Matrix res = worldToView(pyramid);
        res = viewToFlat(pyramid, panelDim).times(res);
        return res;
    }

    public static Color phongLight(Light l, ColorPoint cp, Vec3d normal, Point3D camPos) {    //W UKŁADZIE OBSERWATORA
        HashMap<Integer, Integer> konik;
        Vec3d lnorm = new Vec3d(l.getX() - cp.getX(), l.getY() - cp.getY(), l.getZ() - cp.getZ());
        lnorm.normalize();

        Vec3d norm = new Vec3d(normal);
        Vec3d vnorm = new Vec3d(camPos.getX() - cp.getX(), camPos.getY() - cp.getY(), camPos.getZ() - cp.getZ());
        vnorm.normalize();


        Vec3d halfAngle = new Vec3d();
        halfAngle.set(vnorm);
        halfAngle.add(lnorm);
        halfAngle.normalize();

        Vec3d os = new Vec3d(norm);
        os.mul(2 * norm.dot(vnorm));
        os.sub(vnorm);
        os.normalize();
        double OsdotL = os.dot(lnorm);
        double OnePer255 = 1. / 255.;

        double NdotL = norm.dot(lnorm);
        double HdotN = norm.dot(halfAngle);

        NdotL = Double.compare(NdotL, 0.) < 0 ? 0. : NdotL * cp.kd;
        if (NdotL == 0)
            OsdotL = 0;
        else
            OsdotL = Double.compare(OsdotL, 0.) < 0 ? 0. : Math.pow(OsdotL, cp.g) * cp.ks;
        //NdotL = NdotL < 0. ? 0. : NdotL * cp.kd;
        HdotN = HdotN < 0. ? 0. : Math.pow(HdotN, cp.g) * cp.ks;
        //System.out.println(NdotL + " " + OsdotL);

        //R
        double r = cp.color.getRed() * OnePer255;
        r += NdotL * l.getColor().getRed() * OnePer255;
        r += OsdotL * l.getColor().getRed() * OnePer255;

        if (r < 0)
            r = 0;
        else if (r < 1)
            r = Math.floor(r * 255);
        else r = 255;

        //G
        double g = cp.color.getGreen() * OnePer255;
        g += NdotL * l.getColor().getGreen() * OnePer255;
        g += OsdotL * l.getColor().getGreen() * OnePer255;

        if (g < 0)
            g = 0;
        else if (g < 1)
            g = Math.floor(g * 255);
        else g = 255;

        //B
        double b = cp.color.getBlue() * OnePer255;
        b += NdotL * l.getColor().getBlue() * OnePer255;
        b += OsdotL * l.getColor().getBlue() * OnePer255;

        if (b < 0)
            b = 0;
        else if (b < 1)
            b = Math.floor(b * 255);
        else b = 255;


        return new Color((int) r, (int) g, (int) b);

    }

    public static double[] findBarycentric(double px, double py, double x1, double y1, double x2, double y2, double x3, double y3) { // W 2D
        double a, b, c;

     /*   b = (double) (((x1 - x3) * (py - y3) - px + x3)) /
                ((y2 - y3) * (x1 - x3));

        a = ((px - b * (x2 - x3) - x3)) /
                (x1 - x3);
*/
        Vec2d v0 = new Vec2d(x2 - x1, y2 - y1), v1 = new Vec2d(x3 - x1, y3 - y1), v2 = new Vec2d(px - x1, py - y1);
        double d00 = v0.x * v0.x + v0.y * v0.y;
        double d01 = v0.x * v1.x + v0.y * v1.y;
        double d11 = v1.x * v1.x + v1.y * v1.y;
        double d20 = v0.x * v2.x + v0.y * v2.y;
        double d21 = v2.x * v1.x + v2.y * v1.y;
        double denom = d00 * d11 - d01 * d01;
        a = (d11 * d20 - d01 * d21) / denom;
        b = (d00 * d21 - d01 * d20) / denom;
        c = 1.0 - a - b;
        a = a < 0 ? 0 : a;
        b = b < 0 ? 0 : b;
        c = c < 0 ? 0 : c;
        double[] lambdas = {a, b, c};
        return lambdas;
    }

    public static Color findColorByBar(double l1, double l2, double l3, double r1, double g1, double b1, double r2, double g2, double b2, double r3, double g3, double b3) {
        int r = (int) (r1 * l1) + (int) (r2 * l2) + (int) (r3 * l3);
        if (r < 0)
            r = 0;
        else if (r > 255)
            r = 255;

        int g = (int) (g1 * l1) + (int) (g2 * l2) + (int) (g3 * l3);
        if (g < 0)
            g = 0;
        else if (g > 255)
            g = 255;

        int b = (int) (b1 * l1) + (int) (b2 * l2) + (int) (b3 * l3);
        if (b < 0)
            b = 0;
        else if (b > 255)
            b = 255;
        return new Color(r, g, b);
    }

    public static Vec3d findNormByBar(double l1, double l2, double l3, Vec3d n1, Vec3d n2, Vec3d n3) {
        Vec3d nn1 = new Vec3d(n1);
        Vec3d nn2 = new Vec3d(n2);
        Vec3d nn3 = new Vec3d(n3);
        nn1.mul(l1);
        nn2.mul(l2);
        nn3.mul(l3);
        Vec3d norm = new Vec3d();
        norm.add(nn1, nn2);
        norm.add(nn3);
        norm.normalize();//konieczne?
        return norm;
    }

}