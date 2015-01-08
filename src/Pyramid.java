import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-06.
 */
public class Pyramid {
    Plane plane;
    Point3D[] edges; //top right, bottom right, bottom left, top left chyba
    Vec3d horVec;
    Vec3d verVec;
    Camera camera;
    Vec3d distVec;
    Light light;
    double PROPORTIONS;

    public Pyramid() {
        plane = new Plane();
        edges = new Point3D[4];
        for (int i = 0; i < 4; i++)
            edges[i] = new Point3D(0, 0, 0);
        horVec = new Vec3d();
        verVec = new Vec3d();
        distVec = new Vec3d();
        camera = new Camera();
        light = new Light();
    }

    public Pyramid(double prop) {
        plane = new Plane();
        edges = new Point3D[4];
        for (int i = 0; i < 4; i++)
            edges[i] = new Point3D(0, 0, 0);
        horVec = new Vec3d();
        verVec = new Vec3d();
        distVec = new Vec3d();
        camera = new Camera();
        light = new Light();
        PROPORTIONS = prop;
    }

    public void recalcCamera() {
        calcDist();
        calcPlane();
        calcVec();
        calcEdges();
    }

    private void calcDist() {
        distVec.set(
                camera.getCenter().getX() - camera.getPosition().getX(),
                camera.getCenter().getY() - camera.getPosition().getY(),
                camera.getCenter().getZ() - camera.getPosition().getZ());
    }

    private void calcPlane() {
        double l, m, n, o, p, r;
        l = camera.getCenter().getX();
        m = camera.getCenter().getY();
        n = camera.getCenter().getZ();
        o = distVec.x;
        p = distVec.y;
        r = distVec.z;
        plane.setA(l);
        plane.setB(m);
        plane.setC(n);
        plane.setD(-(l * o + m * p + n * r));
    }

    private void calcVec() {
        double o, p, r, a;
        a = Math.tan(Math.toRadians(camera.getAngle()));
        o = distVec.x;
        p = distVec.y;
        r = distVec.z;
        //horizontal orth to distVec, |horVec| = tan(angle)|horVect|
        horVec.z = Math.sqrt(                       //+
                (a * a * (o * o + p * p + r * r) * o * o) /
                        (o * o + r * r));
        horVec.x = -horVec.z * r / o;                   //-
        horVec.y = 0;

        //vertical: x = 0; orth to distVec, |verVec| = proportions*|horVec|
        verVec.z = Math.sqrt(                       //+
                (PROPORTIONS * PROPORTIONS * (horVec.x * horVec.x + horVec.z * horVec.z) * p * p) /
                        (p * p + r * r));
        verVec.y = -verVec.z * r / o;                   //-
        verVec.x = 0;
    }

    public void recalcLight(Light light) {
        this.light = light;
    }

    private void calcEdges() {
        edges[0] = new Point3D(
                camera.getCenter().getX() + horVec.x + verVec.x,
                camera.getCenter().getY() + horVec.y + verVec.y,
                camera.getCenter().getZ() + horVec.z + verVec.z);
        edges[1] = new Point3D(
                camera.getCenter().getX() - horVec.x + verVec.x,
                camera.getCenter().getY() - horVec.y + verVec.y,
                camera.getCenter().getZ() - horVec.z + verVec.z);
        edges[2] = new Point3D(
                camera.getCenter().getX() - horVec.x - verVec.x,
                camera.getCenter().getY() - horVec.y - verVec.y,
                camera.getCenter().getZ() - horVec.z - verVec.z);
        edges[3] = new Point3D(
                camera.getCenter().getX() + horVec.x - verVec.x,
                camera.getCenter().getY() + horVec.y - verVec.y,
                camera.getCenter().getZ() + horVec.z - verVec.z);
    }


    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public Point3D[] getEdges() {
        return edges;
    }

    public void setEdges(Point3D[] edges) {
        this.edges = edges;
    }

    public Vec3d getHorVec() {
        return horVec;
    }

    public void setHorVec(Vec3d horVec) {
        this.horVec = horVec;
    }

    public Vec3d getVerVec() {
        return verVec;
    }

    public void setVerVec(Vec3d verVec) {
        this.verVec = verVec;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Vec3d getDistVec() {
        return distVec;
    }

    public void setDistVec(Vec3d distVec) {
        this.distVec = distVec;
    }

    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
    }
}
