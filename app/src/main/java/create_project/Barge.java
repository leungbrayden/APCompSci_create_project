package create_project;

import processing.core.PGraphics;
import processing.core.PShape;

public class Barge extends GameObject{

    PShape bargeMesh;

    private final static float depth = 65.5f;
    private final static float half_width = 18.52f;

    public Barge(boolean isRedAlliance) {
        super(new Vector2D[] {}, new Vector2D(0, 144 + depth/2.), 0);
        this.setStatic(false);
        Vector2D[] points = new Vector2D[6];
        // set vertices of collision polygon
        for (int i = 0; i < 6; i++) {
            Vector2D point = new Vector2D(half_width,depth/2.);
            points[i] = point.rotate((float) (i * Math.PI / 3.));
        }

        this.setVertices(points);
        
        // set position based on alliance
        // setPosition(new Vector2D());
    }

    public void loadShape(PShape shape) {
        this.bargeMesh = shape;
    }

    @Override
    public void draw(PGraphics pg) {
        if (!this.isVisible()) {
            return;
        }

        // draw stuff
        pg.pushMatrix();
        pg.shape(bargeMesh);
        pg.popMatrix();
    }
}

