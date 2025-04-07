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
            pg.pushMatrix();
            pg.resetMatrix();
            pg.pushStyle();
            pg.stroke(color);
            pg.strokeWeight(2);
            pg.line(position.x, position.y, position.z,
                    (float) (position.x + vector.getX()), position.y, (float) (position.z + vector.getY()));
            pg.popStyle();
            pg.popMatrix();
        }
    }
}
