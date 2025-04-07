package collision_tests;

import processing.core.PApplet;
import processing.core.PVector;

public class Logger {
    private static PApplet applet;

    public static void init(PApplet applet) {
        Logger.applet = applet;
    }

    public static void displayPoint(PVector point, int color) {
        applet.fill(color);
        applet.stroke(color);
        applet.ellipse(point.x, point.y, 10, 10);
    }

    public static void displayVector(PVector vector, PVector origin, int color) {
        applet.stroke(color);
        applet.strokeWeight(2);
        applet.line(origin.x, origin.y, origin.x + vector.x, origin.y + vector.y);
    }
}
