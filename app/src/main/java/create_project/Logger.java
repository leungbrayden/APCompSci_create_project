package create_project;

import processing.core.PGraphics;
import processing.core.PVector;

public final class Logger {
    private static PGraphics pg;

    public static void init(PGraphics pg) {
        Logger.pg = pg;
    }

    public static void recordVector(Vector2D vector, PVector position, int color) {
        if (Constants.DEBUG) {
            Logger.pg.pushMatrix();
            Logger.pg.resetMatrix();
            Logger.pg.pushStyle();
            Logger.pg.stroke(color);
            Logger.pg.strokeWeight(2);
            Logger.pg.line(position.x, position.y, position.z,
                    (float) (position.x + vector.getX()), position.y, (float) (position.z + vector.getY()));
            Logger.pg.popStyle();
            Logger.pg.popMatrix();
        }
    }

    // public static void recordPoint(PVector point, int color) {
    //     System.out.println("recordPoint: " + point.x + ", " + point.y + ", " + point.z);
    //     if (Constants.DEBUG) {
    //         System.out.println("recordPoint: " + point.x + ", " + point.y + ", " + point.z);
    //         // Logger.pg.pushMatrix();
    //         // Logger.pg.resetMatrix();
    //         // Logger.pg.pushStyle();
    //         Logger.pg.stroke(color);
    //         Logger.pg.background(0xFFFF0000);
    //         Logger.pg.strokeWeight(10);
    //         Logger.pg.fill(color);
    //         Logger.pg.translate(point.x, point.y, point.z);
    //         // Logger.pg.point(point.x, point.y, point.z);
    //         Logger.pg.box(1000);
    //         // Logger.pg.popStyle();
    //         // Logger.pg.popMatrix();
    //     }
    // }
}
