package create_project.GameObjects;

import com.jogamp.graph.geom.Triangle;

import processing.core.PGraphics;
import processing.core.PVector;

public class coral extends GameObject{
 private double width;
    private double height;
    private double depth;

    public coral(PVector position, double weight, double width, double height, double depth) {
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
     private static final float twoPi = (float) Math.PI * 2;
    
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

    public void applyForce(PVector force) {
        PVector acceleration = PVector.div(force, (float) getWeight());
        setAcceleration(acceleration);
    }
    
    public void applyImpulse(PVector impulse) {
        PVector velocity = PVector.div(impulse, (float) getWeight());
        setVelocity(velocity);
    }

    private void cylinderDraw(PGraphics pg, float width, float height){
        int sides = 30;
        float angleSteps = twoPi / sides;

        pg.pushMatrix();
        pg.translate(getPosition().x, getPosition().y, getPosition().z);
        pg.beginShape(TRIANGLE_STRIP);

        for(int i = 0; i <= sides; i++){

        }

        
    }
    
    //public void applyTorque(float torque) {
    //    // Implement the logic for applying torque to the box here
    //}
    
    //public void applyImpulseTorque(float impulseTorque) {
    //    // Implement the logic for applying impulse torque to the box
    //}
}
