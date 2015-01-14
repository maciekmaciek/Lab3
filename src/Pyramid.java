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
    Vec3d upVec;
    Camera camera;
    Vec3d distVec;
    Light light;
    double PROPORTIONS;

    public Pyramid() {
        upVec = new Vec3d(0, 1, 0);
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
        upVec = new Vec3d(0, 1, 0);
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


        horVec.cross(distVec, upVec);
        horVec.normalize();
        double a;
        a = Math.tan(Math.toRadians(camera.getAngle()));
        horVec.mul(a * distVec.length());
        verVec.cross(horVec, distVec);
        verVec.normalize();
        verVec.mul(horVec.length() * PROPORTIONS);
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

    public double getHeight() {
        return 2 * verVec.length();
    }

    public double getWidth() {
        return 2 * horVec.length();
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
