package create_project;

import processing.core.PGraphics;
import processing.core.PShape;

public class Reef extends GameObject{
    PShape reefMesh;

    private final static float depth = 65.5f;
    private final static float half_width = 18.52f;

    public Reef(boolean isRedAlliance) {
        super(new Vector2D[] {}, new Vector2D(0, 144 + depth/2.), 0);
        this.setStatic(true);
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
        this.reefMesh = shape;
    }


    @Override
    public void draw(PGraphics pg) {
        if (!this.isVisible()) {
            return;
        }

        // draw stuff
        pg.pushMatrix();
        pg.translate((float) getPosition().getX(), 0.f, (float) getPosition().getY());
        pg.rotateY((float) Math.PI / 6.f);
        pg.shape(reefMesh);
        pg.popMatrix();

        // if (Constants.DEBUG) {
        //     pg.pushMatrix();
        //     pg.pushStyle();
        //     pg.fill(0xFFFF0000);
        //     pg.translate((float) getPosition().getX(), 0.f, (float) getPosition().getY());
        //     Vector2D[] vertices = this.getVertices();
        //     for (Vector2D vertex : vertices) {
        //         pg.pushMatrix();
        //         pg.translate((float)vertex.getX(), 0.f, (float)vertex.getY());
        //         pg.box(1,10,1);
        //         pg.popMatrix();
        //     }
        //     pg.popStyle();
        //     pg.popMatrix();
        // }
    }
}
 