package create_project;

import create_project.util.Vector2D;
import processing.core.PGraphics;
import processing.core.PVector;

public class Box extends GameObject{
    private double width, height, depth;
    private int colour;

    private float objectY = 0.f;

    public Box(PVector position, double weight, double width, double height, double depth, int colour) {
        super(new Vector2D[] {
            new Vector2D(width * 0.5, depth * 0.5),
            new Vector2D(-width * 0.5, depth * 0.5),
            new Vector2D(-width * 0.5, -depth * 0.5),
            new Vector2D(width * 0.5, -depth * 0.5)
        }, new Vector2D(position.x,position.z), weight);
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.objectY = position.y;
        this.colour = colour;
        this.setInertia(weight * (width * width + depth * depth) / 12.0);
    }

    public Box(PVector position, double weight, double width, double height, double depth, int colour, double rotation) {
        this(position, weight, width, height, depth, colour);
        this.setRotation(rotation);
    }

    @Override
    public void draw(PGraphics pg) {
        pg.pushMatrix();
        pg.fill(colour);
        pg.translate((float) this.getPosition().getX(), (float) objectY, (float) this.getPosition().getY());
        pg.rotateY((float) getRotation());
        pg.box((float) this.getWidth(), (float) this.getHeight(), (float) this.getDepth());
        pg.popMatrix();
    }

    public Box isStatic() {
        this.setStatic(true);
        return this;
    }
    
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }
    
    public double getVolume() {
        return width * height * depth;
    }
    
    public double getSurfaceArea() {
        return 2 * (width * height + width * depth + height * depth);
    }
    
    
    //public void applyTorque(float torque) {
    //    // Implement the logic for applying torque to the box here
    //}
    
    //public void applyImpulseTorque(float impulseTorque) {
    //    // Implement the logic for applying impulse torque to the box
    //}
}
