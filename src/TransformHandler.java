import Jama.Matrix;
import javafx.geometry.Point3D;

import java.awt.*;
import java.util.ArrayList;

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
            result.add(pointToPerspective(p3D, transform,pyramid.camera));
        }
        return result;
    }*/

    public static ColorPoint pointToPerspective(ColorPoint p3D, Matrix transform) {
        Matrix pointM = toMatrix(p3D);
        pointM = transform.times(pointM);

        return new ColorPoint(
                (int)(pointM.get(0,0)/pointM.get(3,0)),
                (int)(pointM.get(1,0)/pointM.get(3,0)),
                (int)(pointM.get(2,0)/pointM.get(3,0)),
                p3D.color
                );
    }


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

    public static Matrix findNormTransformMatrix(Pyramid pyramid, Dimension panelDim) {
        double eps = 0.001;
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

// rzutowanie perspektywiczne
        double d = pyramid.getDistVec().length();

        res = perspective(res, d);
        tempRes = res.times(toMatrix(camera.getPosition()));
        System.out.println(tempRes.get(0,0)/tempRes.get(3,0) + ", " + tempRes.get(1,0)/tempRes.get(3,0) + ", " + tempRes.get(2,0)/tempRes.get(3,0));


// skalowanie do obrazu
        double sx = panelDim.getWidth()/pyramid.getWidth();
        double sy = panelDim.getHeight()/pyramid.getHeight();

        res = scale(res, sx, sy, 1);
        tempRes = res.times(toMatrix(camera.getPosition()));
        System.out.println(tempRes.get(0,0)/tempRes.get(3,0) + ", " + tempRes.get(1,0)/tempRes.get(3,0) + ", " + tempRes.get(2,0)/tempRes.get(3,0));

// przesunięcie do 0.0*/
        double tx = pyramid.getWidth();
        double ty = pyramid.getHeight();

        res = translate(res, tx, ty, 0);

        return res;
    }

/*
    //private static Point
    private static double[][] toMatrix(String trans) { //String do macierzy 3x3
        double[][] matrix = new double[3][3];
        String[] param = trans.split(" ");
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++, k++) {
                matrix[i][j] = Double.parseDouble(param[k]);
            }
        }
        return matrix;
    }

    private static double[][] mergeMatrix(ArrayList<String> transArray) throws Exception { //tworzy finałową macierz przekształceń
        ArrayList<double[][]> matrixArr = new ArrayList<>();
        for (String s : transArray) {
            matrixArr.add(toMatrix(s));
        }
        double[][] resultMatrix = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};

        for (int i = 0; i < matrixArr.size(); i++) {
            resultMatrix = multi(matrixArr.get(i), resultMatrix);
        }
        return resultMatrix;
    }

    private static Point toPoint(double[][] coord) {    //do punktu
        double c0 = coord[0][0];
        double c1 = coord[1][0];
        double c2 = coord[2][0];
        int newX = c0 / c2 < 0 ? (int) (c0 / c2 - EPSILON) : (int) (c0 / c2 + EPSILON);
        int newY = c1 / c2 < 0 ? (int) (c1 / c2 - EPSILON) : (int) (c1 / c2 + EPSILON);
        return new Point(newX, newY);
    }

    private static double[][] toCoord(Point p) {        //do jednorodnych
        return new double[][]{{p.x}, {p.y}, {1}};
    }

    private static double[][] multi(double[][] arr1, double[][] arr2) throws Exception {    //iloczyn wektorowy
        if (arr1[0].length != arr2.length)
            throw new Exception("I don't even...");
        int arr1col = arr1[0].length;
        int row = arr1.length;
        int col = arr2[0].length;
        double[][] result = new double[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                for (int m = 0; m < arr1col; m++)
                    result[i][j] += arr1[i][m] * arr2[m][j];

        return result;
    }


    public static ArrayList<Point> transformVector(ArrayList<Point> points, ArrayList<String> transArray) throws Exception { //całe przekształcenie
        ArrayList<Point> newPoints = new ArrayList<>();
        double[][] transformMatrix = mergeMatrix(transArray);

        for (int i = 0; i < points.size(); i++) {
            double[][] tempCoord = toCoord(points.get(i));
            tempCoord = multi(transformMatrix, tempCoord);
            newPoints.add(toPoint(tempCoord));
        }
        return newPoints;
    }


    public static ArrayList<String> toTransArray(String text) {
        ArrayList<String> resultArr = new ArrayList<>();
        String[] tokenArray = text.split("->");
        String[] temp;
        for (int i = 0; i < tokenArray.length; i++) {
            temp = trim(tokenArray[i]);
            if (tokenArray[i].charAt(0) == 'P') {
                resultArr.add("1 0 " + temp[0] +
                        " 0 1 " + temp[1] +
                        " 0 0 1");
            } else if (tokenArray[i].charAt(0) == 'S') {
                resultArr.add(temp[0] + " 0 0 " +
                        "0 " + temp[1] + " 0 " +
                        "0 0 1");
            } else if (tokenArray[i].charAt(0) == 'O') {
                double rad = Math.toRadians(Double.parseDouble(temp[0]));
                double cosrad = Math.cos(rad);
                double sinrad = Math.sin(rad);
                resultArr.add(Double.toString(cosrad) + " " + Double.toString(-sinrad) + " 0 " +
                        Double.toString(sinrad) + " " + Double.toString(cosrad) + " 0 " +
                        "0 0 1");
            }

        }
        return resultArr;
    }

    private static String[] trim(String s) {
        return s.substring(2, s.length() - 1).split(", ");
    }

    public static String toText(String s) {
        String[] temp = s.split(" ");
        String result;
        if (!temp[1].equals("0")) {
            double deg = Math.toDegrees(Math.acos(Double.parseDouble(temp[0])));
            result = "O(" + deg + ")";
        } else if (!temp[2].equals("0")) {
            result = "P(" + temp[2] + ", " + temp[5] + ")";
        } else {
            result = "S(" + temp[0] + ", " + temp[4] + ")";
        }
        return result;
    }

    public static BufferedImage transformRaster(ArrayList<String> transArray, BufferedImage loadedImage, ArrayList<Point> frame, ArrayList<Point> loadedPoints) throws Exception {
        BufferedImage result;

        double[][] transformMatrix = mergeMatrix(transArray);
        Matrix invertedMatrix = new Matrix(transformMatrix);
        invertedMatrix = invertedMatrix.inverse();
        int[][] pixels = convertTo2D(loadedImage);
        transformMatrix = invertedMatrix.getArray();
        final boolean hasAlphaChannel = loadedImage.getAlphaRaster() != null;
        int offsetOldX = loadedPoints.get(0).x;
        int offsetOldY = loadedPoints.get(0).y;
        int offsetNewX = frame.get(0).x;
        int offsetNewY = frame.get(0).y;
        int newHeight = frame.get(1).y - frame.get(0).y;
        int newWidth = frame.get(3).x - frame.get(0).x;


        int[][] newPixels = new int[newHeight][newWidth];
        double[][] tempCoord = new double[3][1];
        double[][] tempResult = new double[3][1];
        int tempI = 0;
        int tempJ = 0;
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {


                tempCoord[0][0] = j + offsetNewX;// + minW;          //przekształcenie
                tempCoord[1][0] = i + offsetNewY;// + minH;
                tempCoord[2][0] = 1;
                tempResult = multi(transformMatrix, tempCoord);

                tempCoord[0][0] = tempResult[0][0] / tempResult[2][0] + offsetOldX;
                tempCoord[1][0] = tempResult[1][0] / tempResult[2][0] + offsetOldY;
                tempI = (int) tempCoord[1][0];
                tempJ = (int) tempCoord[0][0];
                if (tempI >= 0 && tempJ >= 0 && tempI < pixels.length && tempJ < pixels[0].length) {

                    if (tempJ + 1 >= pixels[0].length)
                        tempJ = pixels[0].length - 2;

                    if (tempI + 1 >= pixels.length)
                        tempI = pixels.length - 2;

                    Color a = new Color(pixels[tempI][tempJ]);
                    Color b = new Color(pixels[tempI][tempJ + 1]);
                    Color c = new Color(pixels[1 + tempI][tempJ]);
                    Color d = new Color(pixels[tempI + 1][tempJ + 1]);
                    int red = twoLineInterpolation(tempCoord[0][0], tempCoord[1][0], a.getRed(), b.getRed(), c.getRed(), d.getRed());
                    int green = twoLineInterpolation(tempCoord[0][0], tempCoord[1][0], a.getGreen(), b.getGreen(), c.getGreen(), d.getGreen());
                    int blue = twoLineInterpolation(tempCoord[0][0], tempCoord[1][0], a.getBlue(), b.getBlue(), c.getBlue(), d.getBlue());


                    if (red < 0) red = 0;
                    if (red > 255) red = 255;
                    if (blue < 0) blue = 0;
                    if (blue > 255) blue = 255;
                    if (green < 0) green = 0;
                    if (green > 255) green = 255;

                    Color res = new Color(red, green, blue);

                    newPixels[i][j] = res.getRGB();

                } else {
                    newPixels[i][j] = 0xffffff;
                }

            }
        }
        result = getImage(newPixels, loadedImage.getAlphaRaster() != null);
        return result;
    }


    private static int twoLineInterpolation(double x, double y, int a, int b, int c, int d) {
        int i = (int) x, j = (int) y;
        double alpha = x - i, beta = y - j;

        double Xab = alpha * b + (1 - alpha) * a;
        double Xcd = alpha * d + (1 - alpha) * c;

        return (int) Math.round(beta * Xcd + (1 - beta) * Xab);
    }

    private static int[][] convertTo2D(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                //result[row][col] = argb;
                result[row][result[0].length - 1 - col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    private static BufferedImage getImage(int[][] pixels, final boolean withAlpha) {
        BufferedImage img = new BufferedImage(pixels[0].length, pixels.length, withAlpha ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_3BYTE_BGR);
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                if (withAlpha)
                    img.setRGB(x, y, pixels[y][x]);
                else {
                    int pixel = pixels[y][x];
                    int alpha = (pixel >> 24 & 0xff);
                    int red = (pixel >> 16 & 0xff);
                    int green = (pixel >> 8 & 0xff);
                    int blue = (pixel & 0xff);
                    int rgb = (red << 16) | (green << 8) | blue;
                    img.setRGB(x, y, rgb);
                }
            }
        }
        return img;
    }
*/
}