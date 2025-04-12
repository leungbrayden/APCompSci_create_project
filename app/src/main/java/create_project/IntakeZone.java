package create_project;

import java.util.List;
import java.util.function.Supplier;

import processing.core.PGraphics;
import processing.core.PVector;

public class IntakeZone {
    private PVector position;
    private double width, height, depth;
    private double rotationX, rotationY, rotationZ = 0;
    
    private boolean active = true;

    public IntakeZone(PVector position, double width, double height, double depth) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public void setRotation(double rotationX, double rotationY, double rotationZ) {
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
    }

    public Coral checkCollision(List<Coral> corals) {
        if (!active) {
            return null;
        }
        for (Coral coral : corals) {
            if (coral.isVisible() && coral.isCollidable()) {
                if (coral.getPosition().getX() > position.x - width / 2. && coral.getPosition().getX() < position.x + width / 2 &&
                    coral.getY() > position.y - height / 2 && coral.getY() < position.y + height / 2 &&
                    coral.getPosition().getY() > position.z - depth / 2. && coral.getPosition().getY() < position.z + depth / 2) {
                    
                    return coral;
                }
            }
        }
        return null;
    }
    // public void update() {

    // }

    public void draw(PGraphics pg) {
        if (!active) {
            return;
        }
        pg.pushMatrix();
        pg.translate(position.x, position.y, position.z);
        pg.rotateY((float) -rotationY);
        pg.rotateX((float) -rotationX);
        pg.rotateZ((float) -rotationZ);
        pg.pushStyle();
        pg.fill(0xAA0000FF);
        pg.box((float) width, (float) height, (float) depth);
        pg.popMatrix();
    }



}
