package create_project.GameObjects;

import processing.core.PVector;
import processing.core.PGraphics;

public abstract class GameObject {
    public PVector position;
    public PVector velocity;
    public PVector acceleration;
    public double weight;

    public void update() {
        velocity.add(acceleration);
        position.add(velocity);
    }

    public abstract void draw(PGraphics pg);

    public PVector getPosition() {
        return position;
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public PVector getVelocity() {
        return velocity;
    }

    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    public PVector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(PVector acceleration) {
        this.acceleration = acceleration;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
