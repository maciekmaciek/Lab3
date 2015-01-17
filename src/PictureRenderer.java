import Jama.Matrix;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-11.
 */
public class PictureRenderer {
    Gui gui;
    double[][] zBuf;
    BufferedImage img;

    public PictureRenderer(Gui gui) {
        this.gui = gui;
        img = new BufferedImage(gui.pPanel.getWidth(), gui.pPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        zBuf = new double[gui.pPanel.getWidth()][gui.pPanel.getHeight()];
        clearBuf();
    }

    //PAMIETAJ O ZAMIANIE Y
    private void clearBuf() {
        for (int i = 0; i < zBuf.length; i++)
            for (int j = 0; j < zBuf[0].length; j++) {
                zBuf[i][j] = Double.POSITIVE_INFINITY;
                img.setRGB(i, j, Color.DARK_GRAY.getRGB());
            }

    }

    public BufferedImage drawxOrtX() {


        return deepCopy(img);
    }


    public BufferedImage drawPersp() {
        ArrayList<Triangle> trList = gui.normalizedData.triangles;
        clearBuf();
        Collections.sort(trList);
        for (int i = 0; i < trList.size(); i++) {
            trList.get(i).transform(gui.viewToFlat);
            if (isInboundTr(trList.get(i))) {
                iterateTriangle(trList.get(i));
            }

        }
        return deepCopy(img);
    }

    private void iterateTriangle(Triangle tr) {
        Triangle[] split = tr.split(gui.viewToFlat);
        System.out.println("--------W ITERATE---------");
        Matrix m = gui.viewToFlat;
        Point3D p3d1 = TransformHandler.applyTransformToPoint(tr.sorted.get(0), m);
        Point3D p3d2 = TransformHandler.applyTransformToPoint(tr.sorted.get(1), m);
        Point3D p3d3 = TransformHandler.applyTransformToPoint(tr.sorted.get(2), m);
        System.out.println(tr.sorted.get(0).getX() + ", " + tr.sorted.get(0).getY() + ", " + tr.sorted.get(0).getZ());
        System.out.println(tr.sorted.get(1).getX() + ", " + tr.sorted.get(1).getY() + ", " + tr.sorted.get(1).getZ());
        System.out.println(tr.sorted.get(2).getX() + ", " + tr.sorted.get(2).getY() + ", " + tr.sorted.get(2).getZ());
        System.out.println(tr.sortedTransformed.get(0).getX() + ", " + tr.sortedTransformed.get(0).getY() + ", " + tr.sortedTransformed.get(0).getZ());
        System.out.println(tr.sortedTransformed.get(1).getX() + ", " + tr.sortedTransformed.get(1).getY() + ", " + tr.sortedTransformed.get(1).getZ());
        System.out.println(tr.sortedTransformed.get(2).getX() + ", " + tr.sortedTransformed.get(2).getY() + ", " + tr.sortedTransformed.get(2).getZ());
        System.out.println(p3d1.getX() + ", " + p3d1.getY() + ", " + p3d1.getZ());
        System.out.println(p3d2.getX() + ", " + p3d2.getY() + ", " + p3d2.getZ());
        System.out.println(p3d3.getX() + ", " + p3d3.getY() + ", " + p3d3.getZ());

        if (split[0] != null) {
            //split[0].transform(gui.viewToFlat);
            //split[0].sort();
            iterateFlatTopTr(
                    split[0], tr);
        }

        if (split[1] != null) {
            //split[1].transform(gui.viewToFlat);
            iterateFlatBottomTr(
                    split[1], tr);
        }


        //pętla
        //wyliczz
        //odwróć rzutownaie
        //jak mniejszy od pixela to
        // licz kolor
        //
    }

    private void iterateFlatBottomTr(Triangle tr2D, Triangle tr3D) {
        ColorPoint v1 = tr2D.sorted.get(0);
        ColorPoint v2 = tr2D.sorted.get(1);
        ColorPoint v3 = tr2D.sorted.get(2);
        double invslope1 = (v3.getX() - v1.getX()) / (v3.getY() - v1.getY());
        double invslope2 = (v3.getX() - v2.getX()) / (v3.getY() - v2.getY());

        double curx1 = v3.getX();
        double curx2 = v3.getX();
        for (int scanlineY = (int) v3.getY(); scanlineY >= v2.getY(); scanlineY--)
        {
            for (int pixelX = (int) curx1; pixelX <= curx2; pixelX++) {
                //System.out.println("flattop: " + pixelX + " " + scanlineY);
                putPixel(pixelX, scanlineY, tr3D);
            }
            curx1 -= invslope1;
            curx2 -= invslope2;
        }
    }

    private void iterateFlatTopTr(Triangle tr2D, Triangle tr3D) {
        ColorPoint v1 = tr2D.sorted.get(0);
        ColorPoint v2 = tr2D.sorted.get(1);
        ColorPoint v3 = tr2D.sorted.get(2);
        double invslope1 = (v2.getX() - v1.getX()) / (v2.getY() - v1.getY());
        double invslope2 = (v3.getX() - v1.getX()) / (v3.getY() - v1.getY());

        double curx1 = v1.getX();
        double curx2 = v1.getX();

        for (int scanlineY = (int) v1.getY(); scanlineY < v3.getY(); scanlineY++) {
            for (int pixelX = (int) curx1; pixelX <= curx2; pixelX++) {
                //System.out.println("flatbottom: " + pixelX + " " + scanlineY);
                putPixel(pixelX, scanlineY, tr3D);
            }
            curx1 += invslope1;
            curx2 += invslope2;
        }
    }


    private void putPixel(int pixelX, int scanlineY, Triangle tr) {
        if (isInboundP(pixelX, scanlineY)) {
            ColorPoint v1 = tr.sortedTransformed.get(0);
            ColorPoint v2 = tr.sortedTransformed.get(1);
            ColorPoint v3 = tr.sortedTransformed.get(2);

            ColorPoint s1 = tr.sorted.get(0);
            ColorPoint s2 = tr.sorted.get(1);
            ColorPoint s3 = tr.sorted.get(2);
/*

            Matrix m = gui.viewToFlat;
            Point3D p3d1 = TransformHandler.applyTransformToPoint(s1, m);
            Point3D p3d2 = TransformHandler.applyTransformToPoint(s2, m);
            Point3D p3d3 = TransformHandler.applyTransformToPoint(s3, m);
            System.out.println("--------W PUT PIXEL---------");
            System.out.println(s1.getX() + ", " + s1.getY()  + ", " + s1.getZ());
            System.out.println(s2.getX() + ", " + s2.getY()  + ", " + s2.getZ());
            System.out.println(s3.getX() + ", " + s3.getY()  + ", " + s3.getZ());
            System.out.println(v1.getX() + ", " + v1.getY()  + ", " + v1.getZ());
            System.out.println(v2.getX() + ", " + v2.getY()  + ", " + v2.getZ());
            System.out.println(v3.getX() + ", " + v3.getY()  + ", " + v3.getZ());
            System.out.println(p3d1.getX() + ", " + p3d1.getY()  + ", " + p3d1.getZ());
            System.out.println(p3d2.getX() + ", " + p3d2.getY()  + ", " + p3d2.getZ());
            System.out.println(p3d3.getX() + ", " + p3d3.getY()  + ", " + p3d3.getZ());

*/



            double bar[] = TransformHandler.findBarycentric(
                    pixelX, scanlineY,
                    (int) v1.getX(), (int) v1.getY(),
                    (int) v2.getX(), (int) v2.getY(),
                    (int) v3.getX(), (int) v3.getY());

            /*double bar3D[] = TransformHandler.findBarycentric(
                    pixelX, scanlineY,
                    (int) s1.getX(), (int) s1.getY(),
                    (int) s2.getX(), (int) s2.getY(),
                    (int) s3.getX(), (int) s3.getY());*/

            /*ColorPoint toView = new ColorPoint(
                    s1.getX() * bar3D[0] + s2.getX() * bar3D[1] + s3.getX() * bar3D[2],
                    s1.getY() * bar3D[0] + s2.getY() * bar3D[1] + s3.getY() * bar3D[2],
                    s1.getZ() * bar3D[0] + s2.getZ() * bar3D[1] + s3.getZ() * bar3D[2],
                    v1.color
            );//jednak nie

            ColorPoint toView = new ColorPoint(
                    s1.getX() * 0.3333333 + s2.getX() * 0.33333333 + s3.getX() * 0.33333,
                    s1.getY() * 0.3333333 + s2.getY() * 0.33333333 + s3.getY() * 0.33333,
                    s1.getZ() * 0.3333333 + s2.getZ() * 0.33333333 + s3.getZ() * 0.33333,
                    v1.color
            );// może w 3D lepiej? nie.*/
            ColorPoint toView = new ColorPoint(
                    s1.getX() * bar[0] + s2.getX() * bar[1] + s3.getX() * bar[2],
                    s1.getY() * bar[0] + s2.getY() * bar[1] + s3.getY() * bar[2],
                    s1.getZ() * bar[0] + s2.getZ() * bar[1] + s3.getZ() * bar[2],
                    v1.color
            );// może w 3D lepiej? nie.
            toView.kd = v1.kd;
            toView.ks = v1.ks;
            toView.g = v1.g;

            //if (isNear(toView.getX(), toView.getY(), toView.getZ(), pixelX, scanlineY)) {
            if (toView.getZ() < zBuf[pixelX][scanlineY]) {
                zBuf[pixelX][scanlineY] = toView.getZ();

                /*toView.color = TransformHandler.findColorByBar(
                        bar[0], bar[1], bar[2],
                        s1.color.getRed(), s1.color.getGreen(), s1.color.getBlue(),
                        s2.color.getRed(), s2.color.getGreen(), s2.color.getBlue(),
                        s3.color.getRed(), s3.color.getGreen(), s3.color.getBlue());
                        */
                //gShading(tr, toView, bar, pixelX, scanlineY);
                pShading(tr, toView, bar, pixelX, scanlineY);

            } //else
            //System.out.println("Nie wpuściłem: [" + pixelX + "][" + scanlineY +"] Old: " + zBuf[pixelX][scanlineY] + " New: " +  toView.getZ());


            /*ColorPoint toView = (ColorPoint) TransformHandler.applyTransformToPoint(new ColorPoint(pixelX, scanlineY, 0, Color.BLACK), gui.viewToFlat.inverse());
            if (toView.getZ() < zBuf[pixelX][scanlineY]) {
                zBuf[pixelX][scanlineY] = toView.getZ();
                double bar[] = TransformHandler.findBarycentric(
                        (int) toView.getX(), (int) toView.getY(),
                        (int) v1.getX(), (int) v1.getY(),
                        (int) v2.getX(), (int) v2.getY(),
                        (int) v3.getX(), (int) v3.getY());
                toView.normal = TransformHandler.findNormByBar(bar[0], bar[1], bar[2], v1.normal, v2.normal, v3.normal);
                toView.color = TransformHandler.findColorByBar(
                        bar[0], bar[1], bar[2],
                        v1.color.getRed(), v1.color.getGreen(), v1.color.getBlue(),
                        v2.color.getRed(), v2.color.getGreen(), v2.color.getBlue(),
                        v3.color.getRed(), v3.color.getGreen(), v3.color.getBlue());

                img.setRGB(
                        pixelX,
                        gui.pPanel.getHeight() - scanlineY,
                        TransformHandler.phongLight(
                                gui.normalizedData.light,
                                toView,
                                gui.normalizedData.camera.getPosition()).getRGB()
                );
            }
            */
        }
    }

    private void pShading(Triangle tr, ColorPoint toView, double[] bar, int x, int y) {
        ColorPoint s1 = tr.sorted.get(0);
        ColorPoint s2 = tr.sorted.get(1);
        ColorPoint s3 = tr.sorted.get(2);
        toView.normal = TransformHandler.findNormByBar(bar[0], bar[1], bar[2], s1.normal, s2.normal, s3.normal);
        int rgb = TransformHandler.phongLight(
                gui.normalizedData.light,
                toView,
                toView.normal,
                gui.normalizedData.camera.getPosition()).getRGB();

        int realY = gui.pPanel.getHeight() - y;
        img.setRGB(
                x,
                realY,
                rgb//test
        );
    }

    private void gShading(Triangle tr, ColorPoint toView, double[] bar, int x, int y) {
        Color s1 = TransformHandler.phongLight(
                gui.normalizedData.light,
                tr.sorted.get(0),
                tr.sorted.get(0).normal,
                gui.normalizedData.camera.getPosition());
        Color s2 = TransformHandler.phongLight(
                gui.normalizedData.light,
                tr.sorted.get(1),
                tr.sorted.get(1).normal,
                gui.normalizedData.camera.getPosition());
        Color s3 = TransformHandler.phongLight(
                gui.normalizedData.light,
                tr.sorted.get(2),
                tr.sorted.get(2).normal,
                gui.normalizedData.camera.getPosition());

        Color result = TransformHandler.findColorByBar(
                bar[0], bar[1], bar[2],
                s1.getRed(), s1.getGreen(), s1.getBlue(),
                s2.getRed(), s2.getGreen(), s2.getBlue(),
                s3.getRed(), s3.getGreen(), s3.getBlue());

        img.setRGB(
                x,
                gui.pPanel.getHeight() - y,
                result.getRGB()
        );

    }

    private boolean isInboundTr(Triangle triangle) {
        int x1, x2, x3, y1, y2, y3;
        x1 = (int) (triangle.sortedTransformed.get(0).getX());
        y1 = (int) (triangle.sortedTransformed.get(0).getY());
        x2 = (int) (triangle.sortedTransformed.get(1).getX());
        y2 = (int) (triangle.sortedTransformed.get(1).getY());
        x3 = (int) (triangle.sortedTransformed.get(2).getX());
        y3 = (int) (triangle.sortedTransformed.get(2).getY());

        if (x1 <= 0 && x2 <= 0 && x3 <= 0)
            return false;
        if (y1 <= 0 && y2 <= 0 && y3 <= 0)
            return false;
        if (x1 >= gui.pPanel.getWidth() && x2 >= gui.pPanel.getWidth() && x3 >= gui.pPanel.getWidth())
            return false;
        if (y1 >= gui.pPanel.getHeight() && y2 >= gui.pPanel.getHeight() - 1 && y3 >= gui.pPanel.getHeight())
            return false;

        return true;
    }

    private boolean isInboundP(int x, int y) {
        if (x >= gui.pPanel.getWidth() || x <= 0) {
            return false;
        }
        if (y >= gui.pPanel.getHeight() || y <= 0) {
            return false;
        }

        return true;
    }

    boolean isNear(double x, double y, double z, int i, int j) {
        Vec3d newDist = new Vec3d(
                gui.normalizedData.camera.getPosition().getX() - x,
                gui.normalizedData.camera.getPosition().getY() - y,
                gui.normalizedData.camera.getPosition().getZ() - z
        );
        double newD = newDist.length();
        if (newD <= zBuf[i][j]) {
            zBuf[i][j] = newD;
            return true;
        } else {
            return false;
        }
    }

    private ArrayList<Triangle> sortFull(ArrayList<Triangle> trList) {
        return null;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}
