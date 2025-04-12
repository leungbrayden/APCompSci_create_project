package collision_tests.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import processing.core.PApplet;

public class Logger {
    private static List<Consumer<PApplet>> dataList = new ArrayList<Consumer<PApplet>>();

    public static void draw(PApplet app) {
        for (Consumer<PApplet> d : dataList) {
            d.accept(app);
        }
        dataList.clear();
    }

    public static void recordVector(Vector2D vector, Vector2D origin, int color) {
        dataList.add(app -> {
            app.stroke(color);
            app.strokeWeight(2);
            app.line((float) origin.getX(), (float) origin.getY(), (float) (origin.getX() + vector.getX()), (float) (origin.getY() + vector.getY()));
        });
    }

    public static void recordVector(Vector2D vector, Vector2D origin) {
        recordVector(vector, origin, 0xFFFFFFFF);
    }

    public static void recordPoint(Vector2D point, int color) {
        dataList.add(app -> {
            app.stroke(color);
            app.strokeWeight(2);
            app.point((float) point.getX(), (float) point.getY());
        });
    }
    
    public static void recordPoint(Vector2D point) {
        recordPoint(point, 0xFFFFFFFF);
    }
}
