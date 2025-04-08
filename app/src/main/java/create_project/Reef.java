package create_project;

import processing.core.PGraphics;
import processing.core.PShape;

public class Reef extends GameObject{
    PShape reefMesh;

    public Reef(boolean isRedAlliance) {
        super(new Vector2D[] {}, new Vector2D(0, 0), 0);
        this.setStatic(true);
        Vector2D[] points = new Vector2D[6];
        // set vertices of collision polygon
        
        // set position based on alliance
        setPosition(new Vector2D());
    }

    public void loadShape(PShape shape) {
        this.reefMesh = shape;
    }

    @Override
    public void draw(PGraphics pg) {
        if (!this.isVisible()) {
            return;
        }

        // draw stuff
        pg.shape(reefMesh);
    }
}
