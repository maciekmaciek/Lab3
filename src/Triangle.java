import java.awt.*;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-01-07.
 */
public class Triangle {
    int a;
    int b;
    int c;
    Color color;
    double kd;  //rozpraszanie
    double ks;  //polyskliwosc
    double g;   //polyskliwosc g

    public Triangle(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Triangle(int a, int b, int c, Color color, double kd, double ks, double g) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.color = color;
        this.kd = kd;
        this.ks = ks;
        this.g = g;
    }
}
