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
            if (coral.isVisible()) {
                if (coral.getPosition().getX() > position.x - width / 2. && coral.getPosition().getX() < position.x + width / 2 &&
                    coral.getY() > position.y - height / 2 && coral.getY() < position.y + height / 2 &&
                    coral.getPosition().getY() > position.z - depth / 2. && coral.getPosition().getY() < position.z + depth / 2) {
                    return coral;
                }
            }
        }
        return null;
    }

    public boolean isActive() {
        return isActive();
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    // public void update() {

    // }

    public void draw(PGraphics pg, double angle) {
        if (!active) {
            pg.pushMatrix();
            Coral.draw(pg, PVector.sub(position,new PVector(0, 4, 0)), 0, this.rotationY, angle);
            pg.popMatrix();
        }
        if (!Constants.DEBUG) {
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

    public void draw(PGraphics pg) {
        draw(pg, 0);
    }

    public PVector getPosition() {
        return position;
    }



}
