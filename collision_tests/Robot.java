package collision_tests;

import collision_tests.util.Logger;
import collision_tests.util.Vector2D;

public class Robot extends GameObject {
    private static final double MAX_SPEED = 186.0; // Maximum speed of the robot
    private static final double kP = 50.0;
    public Robot() {
        super(new Vector2D[] {
            new Vector2D(25, 25),
            new Vector2D(-25, 25),
            new Vector2D(-25, -25),
            new Vector2D(25, -25)
        });
    }

    public void move(Vector2D direction) {
        // double speed = direction.magnitude();
        // Logger.recordVector(movement, getPosition(), 0xFFFFFFFF);

        this.setAcceleration(direction.copy().scale((MAX_SPEED-this.getVelocity().magnitude())*kP));
        
        // applyImpulse(direction,  Vector2D.mult(direction,(MAX_SPEED-this.getVelocity().magnitude())*100.0));
        Logger.recordVector(direction.scale(MAX_SPEED), getPosition(), 0xFFFF0000);
        Logger.recordVector(this.getVelocity(), getPosition(), 0xFF00FF00);
    }
    
}
