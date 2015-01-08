/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2014-12-31.
 */

import javafx.geometry.Point3D;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public final class DataManager {

    private DataManager() {

    }

    public static DrawnData loadData(String current_file_path) {
        Camera cam = new Camera();
        Light lig = new Light();
        ArrayList<Triangle> triangles = new ArrayList<>();
        ArrayList<Point3D> points = new ArrayList<>();

        if (current_file_path.endsWith("cam")) {
            cam = loadCam(current_file_path);
        } else if (current_file_path.endsWith("brp")) {
            cam = loadCam(current_file_path.substring(0, current_file_path.length() - 3) + "cam");
        }

        current_file_path = current_file_path.substring(0, current_file_path.length() - 3) + "brp";

        int numP;
        int numT;
        BufferedReader br;
        String line;
        StringTokenizer token;
        try {
            br = new BufferedReader(new FileReader(new File(current_file_path)));

            numP = Integer.parseInt(br.readLine());
            for (int i = 0; i < numP; i++) {
                line = br.readLine();
                token = new StringTokenizer(line, " ");
                points.add(new Point3D(
                        Double.parseDouble(token.nextToken()),
                        Double.parseDouble(token.nextToken()),
                        Double.parseDouble(token.nextToken())));
            }
            br.readLine();

            numT = Integer.parseInt(br.readLine());
            for (int i = 0; i < numT; i++) {
                line = br.readLine();
                token = new StringTokenizer(line, " ");
                triangles.add(new Triangle(
                        Integer.parseInt(token.nextToken()),
                        Integer.parseInt(token.nextToken()),
                        Integer.parseInt(token.nextToken())));
            }
            br.readLine();

            for (int i = 0; i < numT; i++) {
                line = br.readLine();
                token = new StringTokenizer(line, " ");
                triangles.get(i).color = new Color(
                        Integer.parseInt(token.nextToken()),
                        Integer.parseInt(token.nextToken()),
                        Integer.parseInt(token.nextToken()));
                triangles.get(i).kd =
                        Integer.parseInt(token.nextToken());
                triangles.get(i).ks =
                        Integer.parseInt(token.nextToken());
                triangles.get(i).g =
                        Integer.parseInt(token.nextToken());

            }
            br.readLine();
            line = br.readLine();
            token = new StringTokenizer(line, " ");
            lig = new Light();

            lig.setPosition(new Point3D(
                    Double.parseDouble(token.nextToken()),
                    Double.parseDouble(token.nextToken()),
                    Double.parseDouble(token.nextToken())));

            lig.setColor(new Color(
                    Integer.parseInt(token.nextToken()),
                    Integer.parseInt(token.nextToken()),
                    Integer.parseInt(token.nextToken())));

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new DrawnData(lig, cam, points, triangles);
    }

    private static Camera loadCam(String camPath) {
        File f = new File(camPath);
        Camera camera = new Camera();
        BufferedReader br;
        String line;
        try {
            br = new BufferedReader(new FileReader(f));

            line = br.readLine();
            StringTokenizer token = new StringTokenizer(line, " ");
            camera.setPosition(new Point3D(
                    Double.parseDouble(token.nextToken()),
                    Double.parseDouble(token.nextToken()),
                    Double.parseDouble(token.nextToken())));

            line = br.readLine();
            token = new StringTokenizer(line, " ");
            camera.setCenter(new Point3D(
                    Double.parseDouble(token.nextToken()),
                    Double.parseDouble(token.nextToken()),
                    Double.parseDouble(token.nextToken())));

            camera.setAngle(Integer.parseInt(br.readLine()));
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return camera;
    }

    public static void saveData(String filePath, String newPath, Light light, Camera camera) {
        File f = new File(filePath);
        BufferedReader br;
        BufferedWriter bw;
        try {
            if (newPath.endsWith("cam")) {
                filePath = filePath.substring(0, filePath.length() - 3) + "cam";
                saveCam(filePath, newPath, camera);
                filePath = filePath.substring(0, filePath.length() - 3) + "brp";
                newPath = newPath.substring(0, newPath.length() - 3) + "brp";
                saveBRP(filePath, newPath, light);

            } else if (newPath.endsWith("brp")) {
                filePath = filePath.substring(0, filePath.length() - 3) + "brp";
                saveBRP(filePath, newPath, light);
                filePath = filePath.substring(0, filePath.length() - 3) + "cam";
                newPath = newPath.substring(0, newPath.length() - 3) + "cam";
                saveCam(filePath, newPath, camera);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveBRP(String filePath, String newPath, Light light) throws IOException {
        String line = "";
        String toFile = "";
        File f = new File(filePath);
        BufferedReader br;
        BufferedWriter bw;
        br = new BufferedReader(new FileReader(f));
        if (!f.exists())
            f.createNewFile();

        while (!line.equals("-LIGHT-")) {
            line = br.readLine();
            toFile += line + System.lineSeparator();
        }
        br.close();

        bw = new BufferedWriter(new FileWriter(f, false));
        f = new File(newPath);
        if (!f.exists())
            f.createNewFile();
        bw.write(toFile);
        bw.write(
                light.getPosition().getX() + " " +
                        light.getPosition().getY() + " " +
                        light.getPosition().getZ() + " " +
                        light.getColor().getRed() + " " +
                        light.getColor().getGreen() + " " +
                        light.getColor().getBlue() + " ");
        bw.close();


    }

    private static void saveCam(String filePath, String newPath, Camera camera) throws IOException {
        BufferedWriter bw;
        File f = new File(newPath);
        if (!f.exists())
            f.createNewFile();
        bw = new BufferedWriter(new FileWriter(f, false));

        bw.write(
                camera.getPosition().getX() + " " +
                        camera.getPosition().getY() + " " +
                        camera.getPosition().getZ() + System.lineSeparator() +
                        camera.getCenter().getX() + " " +
                        camera.getCenter().getY() + " " +
                        camera.getCenter().getZ() + System.lineSeparator() +
                        camera.getAngle());
        bw.close();

    }
}
