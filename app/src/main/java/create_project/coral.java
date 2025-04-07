package create_project;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;

public class Coral extends GameObject{

    private final static float width = 11.875f;
    private final static float radius = 2.25f;


    public Coral(PVector position) {
        super(new Vector2D[] {
            new Vector2D(width/2, radius),
            new Vector2D(width/2, -radius),
            new Vector2D(-width/2, -radius),
            new Vector2D(-width/2, radius),
        }, new Vector2D(position.x, position.z), 1);
    }

    @Override
    public void draw(PGraphics pg) {
        // draw cylinder
        pg.pushMatrix();
        pg.translate((float) getPosition().getX(), 2.0f, (float)getPosition().getY());
        pg.rotateY((float) getRotation());
        pg.fill(0xFFFFFFFF);
        pg.beginShape(18);
        for (int i = 0; i < 20; i++) {
            pg.vertex(width/2.f, (float) Math.sin((Math.PI*2) / (i / 20.f)) * radius, (float) Math.cos((Math.PI*2) / (i / 20.f)) * radius);
            pg.vertex(-width/2.f, (float) Math.sin((Math.PI*2) / (i / 20.f)) * radius, (float) Math.cos((Math.PI*2) / (i / 20.f)) * radius);
        }
        pg.endShape(2);
        pg.popMatrix();
    }





}
