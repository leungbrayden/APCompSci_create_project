package create_project;

import processing.core.PVector;

public abstract class GameObject {
    private PVector position;
    private PVector velocity;
    private PVector acceleration;

    GameObject(PVector position) {
        this.position = position;
        this.velocity = new PVector(0, 0, 0);
        this.acceleration = new PVector(0, 0, 0);
    }

    public void update() {
        velocity.add(acceleration);
        position.add(velocity);
    }

    abstract void draw();

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

}
