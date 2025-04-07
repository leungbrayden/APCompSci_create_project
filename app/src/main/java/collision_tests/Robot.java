package collision_tests;

import processing.core.PVector;

public class Robot extends GameObject {
    private boolean isMoving;
    private PVector direction = new PVector(0, 0);
    private int id;

    private static final float MAX_SPEED = 5;
    private static final float MAX_ACCEL = 3 / 60f;

    public Robot(int id) {
        super(new PVector(20, 0), 1., 50, 50);
        this.isMoving = false;
        this.id = id;
        // this.applyImpulse(new PVector(10,10), new PVector(-5, -5));
    }

    public void move(PVector direction) {
        this.isMoving = true;
        this.direction = direction;
    }

    public void rotate(float angle) {
        this.addRotation(angle);
    }

    public void update() {
        Logger.displayPoint(this.getPosition(), 0xFF00FF00);
        Logger.displayPoint(PVector.add(this.getPosition(),this.getVertices().get(1).toPVector()), 0xFF00FF00);
        // accelerate to max speed, can freely turn
        if (this.isMoving) {
            this.applyImpulse(direction.mult(0.05f), new PVector(0, 0));
        }
        super.update();
    }

    public void stop() {
        this.isMoving = false;
    }

    public int getId() {
        return id;
    }
    
}
