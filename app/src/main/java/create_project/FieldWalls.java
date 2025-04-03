package create_project.GameObjects;

import processing.core.PGraphics;
import processing.core.PVector;

public class FieldWalls extends GameObject{
    private double width;
    private double height;
    private double depth;

    public FieldWalls(PVector position, double weight, double width, double height, double depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.position = position;
    }

    public void draw(PGraphics pg) {
        pg.pushMatrix();
        pg.fill(127);
        pg.translate(getPosition().x, getPosition().y, getPosition().z);
        pg.box((float) width, (float) height, (float) depth);
        pg.popMatrix();
    }
}
