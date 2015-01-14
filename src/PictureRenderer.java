import javafx.geometry.Point3D;
import javafx.util.Pair;

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

    public PictureRenderer(Gui gui){
        this.gui = gui;
        img = new BufferedImage(gui.xPanel.getWidth(), gui.xPanel.getHeight(),BufferedImage.TYPE_INT_RGB);
        zBuf = new double[gui.xPanel.getWidth()][gui.xPanel.getHeight()];
        clearBuf();
    }
//PAMIETAJ O ZAMIANIE Y
    private void clearBuf() {
        for(int i = 0; i < zBuf.length; i++)
            for(int j = 0; j < zBuf[0].length; j++) {
                zBuf[i][j] = Double.POSITIVE_INFINITY;
                img.setRGB(i,j, Color.DARK_GRAY.getRGB());
            }

    }

    public BufferedImage drawxOrtX(){


        return deepCopy(img);
    }


    public BufferedImage drawPersp(){
        ArrayList<Triangle> trList = gui.normalizedData.triangles;
        clearBuf();
        Collections.sort(trList);
        for(int i = 0; i<trList.size(); i++){
            if(isInboundTr(trList.get(i))){
                trList.get(i).transform(gui.viewToFlat);
                iterateTriangle(trList.get(i));

                ;
            }

        }
        return deepCopy(img);
    }

    private void iterateTriangle(Triangle tr) {
        Triangle[] split = tr.split();
        //pętla
            //wyliczz
            //odwróć rzutownaie
                //jak mniejszy od pixela to
                    // licz kolor
                    //
    }

    private boolean isInboundTr(Triangle triangle) {
        int x1, x2, x3, y1, y2, y3;
        x1 = (int)(triangle.sortedTransformed.get(0).getX());
        y1 = (int)(triangle.sortedTransformed.get(0).getY());
        x2 = (int)(triangle.sortedTransformed.get(1).getX());
        y2 = (int)(triangle.sortedTransformed.get(1).getY());
        x3 = (int)(triangle.sortedTransformed.get(2).getX());
        y3 = (int)(triangle.sortedTransformed.get(2).getY());

        if(x1 < 0 && x2 < 0 && x3 <0)
            return false;
        if(y1 < 0 && y2 < 0 && y3 <0)
            return false;
        if(x1 > gui.pPanel.getWidth() && x2 > gui.pPanel.getWidth() && x3 > gui.pPanel.getWidth())
            return false;
        if(y1 > gui.pPanel.getHeight() && y2 > gui.pPanel.getHeight() && y3 > gui.pPanel.getHeight())
            return false;

        return true;
    }

    private boolean isInboundP(Point3D p3D){
        if(p3D.getX() > gui.pPanel.getWidth() || p3D.getX() < 0){
            return false;
        }
        if(p3D.getY() > gui.pPanel.getHeight() || p3D.getY() < 0){
            return false;
        }

        return true;

    }

    private void putPixel(double[] bar, ColorPoint a, ColorPoint b, ColorPoint c, ColorPoint p){
       // ColorPoint cp = new ColorPoint()
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
